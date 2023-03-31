package com.andrejanesic.cads.homework1.resultRetriever.impl;

import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.job.query.Query;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

@Singleton
public class ResultRetriever extends IResultRetriever {

    @NonNull
    private final IConfig config;

    @Inject
    public ResultRetriever(IConfig config) {
        this.config = config;
    }

    @Override
    public void main() {

    }

    @Override
    public Future<Result> submit(Query query) {
        if (getCache().containsKey(query)) {
            return getCache().get(query);
        }

        ResultRetrieverCallable callable = new ResultRetrieverCallable(
                this,
                query,
                config.getConfig().keywords()
        );

        Future<Result> res = getPool().submit(callable);
        getCache().put(query, res);
        return res;
    }
}
