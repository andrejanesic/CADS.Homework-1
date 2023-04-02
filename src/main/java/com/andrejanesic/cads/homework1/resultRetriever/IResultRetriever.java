package com.andrejanesic.cads.homework1.resultRetriever;

import com.andrejanesic.cads.homework1.core.ThreadPoolThreadedComponent;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.query.Query;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.job.type.FileJob;
import com.andrejanesic.cads.homework1.job.type.WebJob;
import com.andrejanesic.cads.homework1.scanner.IFileScanner;
import com.andrejanesic.cads.homework1.scanner.IWebScanner;
import lombok.Data;
import lombok.Getter;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public abstract class IResultRetriever extends
        ThreadPoolThreadedComponent<Future<Result>> {

    /**
     * Cache of query results for faster future retrieval.
     */
    @Getter
    private final ConcurrentHashMap<Integer, QueryFutureResult> storeCache
            = new ConcurrentHashMap<>();
    /**
     * Stores file jobs and their respective future results. New entries
     * should be added by {@link IFileScanner} upon receiving a job.
     */
    @Getter
    private final ConcurrentHashMap<String, IJobFutureResult> storeFileJobs =
            new ConcurrentHashMap<>();
    /**
     * Stores web jobs and their respective future results. New entries
     * should be added by {@link IWebScanner} upon receiving a job.
     */
    @Getter
    private final ConcurrentHashMap<String, IJobFutureResult> storeWebJobs =
            new ConcurrentHashMap<>();
    /**
     * Index of all corpus directories (does not include their subdirectories.)
     */
    @Getter
    private final ConcurrentHashMap<String, Boolean> indexedDirectories =
            new ConcurrentHashMap<>();
    /**
     * Index of all websites scanned (only top-level domains, does not
     * include their page URLs).
     */
    @Getter
    private final ConcurrentHashMap<String, Boolean> indexedWebsites =
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
    public void invalidateStores(Query query) {
        // TODO add invalidate cache calls to scanners
        // TODO wire res retriever with commands via routines
        // TODO add method for updating cache: for a given query, a new
        //  aggregate result is calculated from the old aggregate result plus
        //  the latest addition result (called by scanners)
        synchronized (storeCache) {
            Iterator<Integer> it = storeCache.keySet().iterator();
            while (it.hasNext()) {
                QueryFutureResult qf = storeCache.get(it.next());
                if (qf == null) continue;
                if (!Objects.equals(qf.query.getType(), query.getType()))
                    continue;
                if (
                        Objects.equals(qf.query.getId(), query.getId()) ||
                                qf.query.getUri()
                                        .matcher(query.getUri().toString())
                                        .matches()
                ) {
                    it.remove();
                }
            }
        }
        synchronized (storeFileJobs) {
            Iterator<String> it = storeFileJobs.keySet().iterator();
            while (it.hasNext()) {
                IJobFutureResult jf = storeFileJobs.get(it.next());
                if (jf == null) continue;
                if (!Objects.equals(jf.job.getType(), query.getType()))
                    continue;
                if (
                        Objects.equals(jf.job.getId(), query.getId()) ||
                                (query.getUri() != null &&
                                        ((FileJob) jf.getJob()).getPath() != null &&
                                        ((FileJob) jf.job).getPath().contains(
                                                query.getUri().toString())
                                )
                ) {
                    it.remove();
                }
            }
        }
        synchronized (storeWebJobs) {
            Iterator<String> it = storeWebJobs.keySet().iterator();
            while (it.hasNext()) {
                IJobFutureResult jf = storeWebJobs.get(it.next());
                if (jf == null) continue;
                if (!Objects.equals(jf.job.getType(), query.getType()))
                    continue;
                if (
                        Objects.equals(jf.job.getId(), query.getId()) ||
                                (query.getUri() != null &&
                                        ((WebJob) jf.getJob()).getUrl() != null &&
                                        query.getUri().matcher(
                                                        ((WebJob) jf.job).getUrl())
                                                .matches()
                                )
                ) {
                    it.remove();
                }
            }
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
