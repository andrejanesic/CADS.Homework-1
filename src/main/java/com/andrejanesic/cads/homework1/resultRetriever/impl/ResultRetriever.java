package com.andrejanesic.cads.homework1.resultRetriever.impl;

import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.exceptionHandler.IExceptionHandler;
import com.andrejanesic.cads.homework1.job.query.Query;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Singleton
public class ResultRetriever extends IResultRetriever {

    @NonNull
    private final IConfig config;

    private final IExceptionHandler exceptionHandler;

    /**
     * @param config config
     * @deprecated use the new default constructor:
     * {@link #ResultRetriever(IConfig, IExceptionHandler)}
     */
    public ResultRetriever(IConfig config) {
        this(config, null);
    }

    /**
     * Default constructor.
     *
     * @param config           config
     * @param exceptionHandler exception handler
     */
    @Inject
    public ResultRetriever(
            @NonNull IConfig config,
            IExceptionHandler exceptionHandler
    ) {
        this.config = config;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void main() {

    }

    @Override
    public Future<Result> submit(Query query) {
        QueryFutureResult cached = getStoreCache().computeIfPresent(query.hashCode(),
                (i, qf) -> {
                    if (qf.getFuture().isDone() || qf.getFuture().isCancelled()) {
                        try {
                            qf.getFuture().get().setCached(true);
                        } catch (InterruptedException | ExecutionException e) {
                            ;
                        }
                    }
                    return qf;
                });
        if (cached != null)
            return cached.getFuture();

        ResultRetrieverCallable callable = new ResultRetrieverCallable(
                this,
                query,
                config.getConfig().keywords(),
                exceptionHandler
        );

        Future<Result> res = getPool().submit(callable);
        getStoreCache().putIfAbsent(query.hashCode(), new QueryFutureResult(
                query,
                res
        ));
        return res;
    }
}
