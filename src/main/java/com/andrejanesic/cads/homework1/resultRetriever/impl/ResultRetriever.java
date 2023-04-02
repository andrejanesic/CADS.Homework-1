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
        if (getCache().containsKey(query)) {
            Future<Result> fr = getCache().get(query);
            if (fr.isDone() || fr.isCancelled()) {
                try {
                    fr.get().setCached(true);
                } catch (InterruptedException | ExecutionException e) {
                    ;
                }
            }
            return fr;
        }

        ResultRetrieverCallable callable = new ResultRetrieverCallable(
                this,
                query,
                config.getConfig().keywords(),
                exceptionHandler
        );

        Future<Result> res = getPool().submit(callable);
        getCache().put(query, res);
        return res;
    }
}
