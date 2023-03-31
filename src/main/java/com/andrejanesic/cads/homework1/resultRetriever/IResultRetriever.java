package com.andrejanesic.cads.homework1.resultRetriever;

import com.andrejanesic.cads.homework1.core.ThreadPoolThreadedComponent;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.query.Query;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.scanner.IFileScanner;
import com.andrejanesic.cads.homework1.scanner.IWebScanner;
import lombok.Getter;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public abstract class IResultRetriever extends
        ThreadPoolThreadedComponent<Future<Result>> {

    /**
     * Stores file jobs and their respective future results. New entries
     * should be added by {@link IFileScanner} upon receiving a job.
     */
    @Getter
    private ConcurrentHashMap<IJob, Future<Result>> storeFileJobs =
            new ConcurrentHashMap<>();

    /**
     * Stores web jobs and their respective future results. New entries
     * should be added by {@link IWebScanner} upon receiving a job.
     */
    @Getter
    private ConcurrentHashMap<IJob, Future<Result>> storeWebJobs =
            new ConcurrentHashMap<>();

    /**
     * Cache of query results for faster future retrieval.
     */
    @Getter
    private final ConcurrentHashMap<Query, Future<Result>> cache
            = new ConcurrentHashMap<>();

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
        Iterator<Query> it = cache.keys().asIterator();
        while (it.hasNext()) {
            Query k = it.next();
            if (k.getType() == null || query.getType() == null) continue;
            if (!k.getType().equals(query.getType())) continue;
            if (k.getUri() == null || query.getUri() == null) continue;
            if (!query.getUri().matcher(query.getUri().toString()).matches())
                continue;
            if (!Objects.equals(query.getId(), k.getId())) continue;
            it.remove();
        }
    }
}
