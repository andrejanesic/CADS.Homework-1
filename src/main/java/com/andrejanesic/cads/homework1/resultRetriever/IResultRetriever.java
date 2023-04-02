package com.andrejanesic.cads.homework1.resultRetriever;

import com.andrejanesic.cads.homework1.core.ThreadPoolThreadedComponent;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.query.Query;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.scanner.IFileScanner;
import com.andrejanesic.cads.homework1.scanner.IWebScanner;
import lombok.Data;
import lombok.Getter;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public abstract class IResultRetriever extends
        ThreadPoolThreadedComponent<Future<Result>> {

    /**
     * Cache of query results for faster future retrieval.
     */
    @Getter
    private final ConcurrentHashMap<Integer, QueryFutureResult> cache
            = new ConcurrentHashMap<>();
    /**
     * Stores file jobs and their respective future results. New entries
     * should be added by {@link IFileScanner} upon receiving a job.
     */
    @Getter
    private ConcurrentHashMap<String, IJobFutureResult> storeFileJobs =
            new ConcurrentHashMap<>();
    /**
     * Stores web jobs and their respective future results. New entries
     * should be added by {@link IWebScanner} upon receiving a job.
     */
    @Getter
    private ConcurrentHashMap<String, IJobFutureResult> storeWebJobs =
            new ConcurrentHashMap<>();

    /**
     * Requests the IResultRetriever to process the query and return a final
     * result.
     *
     * @param query parameters of the search
     * @return the aggregated {@link Result}. May be incomplete or have
     * exceptions depending on the type of query.
     */
    public abstract Future<Result> submit(Query query);

    /**
     * Removes the cache entries for the given query.
     *
     * @param query query to remove cache for
     */
    public void invalidateCache(Query query) {
        // TODO add invalidate cache calls to scanners
        // TODO wire res retriever with commands via routines
        // TODO add method for updating cache: for a given query, a new
        //  aggregate result is calculated from the old aggregate result plus
        //  the latest addition result (called by scanners)
        final Iterator<Integer> it = cache.keySet().iterator();
        while (it.hasNext()) {
            QueryFutureResult qf = cache.get(it.next());
            if (qf == null) continue;
            if (!qf.query.getUri().matcher(
                    query.getUri().toString()
            ).matches()) continue;
            it.remove();
        }
    }

    @Data
    public static class QueryFutureResult {
        private final Query query;
        private final Future<Result> future;
    }

    @Data
    public static class IJobFutureResult {
        private final IJob job;
        private final Future<Result> future;
    }
}
