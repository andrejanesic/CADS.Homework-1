package com.andrejanesic.cads.homework1.resultRetriever.impl;

import com.andrejanesic.cads.homework1.core.exceptions.ScannerException;
import com.andrejanesic.cads.homework1.core.exceptions.UnexpectedRuntimeComponentException;
import com.andrejanesic.cads.homework1.exceptionHandler.IExceptionHandler;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.JobType;
import com.andrejanesic.cads.homework1.job.query.Query;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.job.type.FileJob;
import com.andrejanesic.cads.homework1.job.type.WebJob;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;
import lombok.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Used by {@link ResultRetriever} to generate job results based on the given
 * parameters asynchronously.
 */
public class ResultRetrieverCallable implements Callable<Result> {

    @NonNull
    private final IResultRetriever resultRetriever;
    @NonNull
    private final Query query;
    @NonNull
    private final String[] keywords;
    private final IExceptionHandler exceptionHandler;

    /**
     * @param resultRetriever {@link IResultRetriever} that spawned this
     * @param query           {@link Query} containing parameters on how to
     *                        fetch the results
     * @param keywords        array of keywords to search for
     * @deprecated use the new default constructor:
     * {@link #ResultRetrieverCallable(IResultRetriever, Query, String[], IExceptionHandler)}
     */
    public ResultRetrieverCallable(
            @NonNull IResultRetriever resultRetriever,
            @NonNull Query query,
            @NonNull String[] keywords
    ) {
        this(resultRetriever, query, keywords, null);
    }

    /**
     * Default constructor.
     *
     * @param resultRetriever  {@link IResultRetriever} that spawned this
     * @param query            {@link Query} containing parameters on how to
     *                         fetch the results
     * @param keywords         array of keywords to search for
     * @param exceptionHandler exception handler
     */
    public ResultRetrieverCallable(
            @NonNull IResultRetriever resultRetriever,
            @NonNull Query query,
            @NonNull String[] keywords,
            IExceptionHandler exceptionHandler
    ) {
        this.resultRetriever = resultRetriever;
        this.query = query;
        this.keywords = keywords;
        this.exceptionHandler = exceptionHandler;

        if (query.getType() == null) {
            RuntimeException ex = new UnexpectedRuntimeComponentException(
                    "ResultRetrieverCallable::call query.type must not be null"
            );
            if (exceptionHandler == null)
                throw ex;
            exceptionHandler.handle(ex);
        }

        if (!query.getType().equals(JobType.FILE) &&
                !query.getType().equals(JobType.WEB)) {
            RuntimeException ex = new UnexpectedRuntimeComponentException(
                    "ResultRetrieverCallable::call query.type must be JobType" +
                            ".FILE or JobType.WEB"
            );
            if (exceptionHandler == null)
                throw ex;
            exceptionHandler.handle(ex);
        }

    }

    @Override
    public Result call() throws Exception {
        ConcurrentHashMap<IJob, Future<Result>> store;
        Object resultLock = new Object();
        Result aggrResult = new Result();
        aggrResult.setExceptions(new HashSet<>());
        long startedTime = System.currentTimeMillis();

        if (query.getType().equals(JobType.FILE)) {
            store = resultRetriever.getStoreFileJobs();
        } else if (query.getType().equals(JobType.WEB)) {
            store = resultRetriever.getStoreWebJobs();
        } else {
            RuntimeException ex = new UnexpectedRuntimeComponentException(
                    "ResultRetrieverCallable::call unexpected query.type=" +
                            query.getType().name()
            );

            if (exceptionHandler == null)
                throw ex;
            exceptionHandler.handle(ex);

            aggrResult.setSuccess(false);
            aggrResult.getExceptions().add(new ScannerException(ex));
            long endTime = System.currentTimeMillis();
            aggrResult.setConsumedTime(endTime - startedTime);
            aggrResult.setConsumedTime(endTime);
            aggrResult.setFrequency(new HashMap<>());
            return aggrResult;
        }

        store.forEach((k, v) -> {
            IJob job;
            if (query.getType().equals(JobType.FILE)) {
                FileJob fileJob = (FileJob) k;
                if (!query.getUri().matcher(fileJob.getPath()).matches())
                    return;
                job = fileJob;
            } else if (query.getType().equals(JobType.WEB)) {
                WebJob webJob = (WebJob) k;
                if (!query.getUri().matcher(webJob.getUrl()).matches())
                    return;
                job = webJob;
            } else {
                aggrResult.getExceptions().add(
                        new ScannerException(
                                "ResultRetrieverCallable::call " +
                                        "unexpected query.type=" +
                                        query.getType().name()
                        )
                );
                return;
            }

            if (query.isWait()) {
                if (v.isCancelled() || !v.isDone()) return;
            }

            Result localResult = null;

            try {
                localResult = v.get();
            } catch (InterruptedException | ExecutionException e) {
                aggrResult.getExceptions().add(
                        new ScannerException(e)
                );
            }

            if (localResult == null) return;

            // add to aggregate
            synchronized (resultLock) {
                aggrResult.getJobs().add(job);
                aggrResult.getExceptions().addAll(localResult.getExceptions());
                aggrResult.setCompletionTime(Math.max(
                        aggrResult.getCompletionTime(),
                        localResult.getCompletionTime()
                ));
                aggrResult.setConsumedTime(
                        aggrResult.getConsumedTime() +
                                localResult.getConsumedTime()
                );
                for (String kw : keywords) {
                    aggrResult.getFrequency().put(
                            kw, aggrResult.getFrequency()
                                    .getOrDefault(kw, 0) +
                                    localResult.getFrequency()
                                            .getOrDefault(kw, 0)
                    );
                }
            }
        });

        aggrResult.setSuccess(aggrResult.getExceptions().isEmpty());
        long endTime = System.currentTimeMillis();
        aggrResult.setConsumedTime(endTime - startedTime);
        aggrResult.setConsumedTime(endTime);
        return aggrResult;
    }
}
