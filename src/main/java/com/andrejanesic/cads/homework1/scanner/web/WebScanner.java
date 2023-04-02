package com.andrejanesic.cads.homework1.scanner.web;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;
import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.RuntimeComponentException;
import com.andrejanesic.cads.homework1.core.exceptions.ScannerException;
import com.andrejanesic.cads.homework1.exceptionHandler.IExceptionHandler;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.job.type.WebJob;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;
import com.andrejanesic.cads.homework1.scanner.IWebScanner;
import com.andrejanesic.cads.homework1.utils.LoopRunnable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Singleton
public class WebScanner extends IWebScanner {

    private final IResultRetriever resultRetriever;
    private final IJobQueue jobQueue;
    private final IConfig config;
    private final IExceptionHandler exceptionHandler;
    private final ICLOutput iclOutput;
    private Map<String, Future<Result>> indexId = new HashMap<>();
    private Map<String, Future<Result>> indexUrl = new HashMap<>();
    private Thread refreshThread;
    private LoopRunnable refreshRunnable;

    /**
     * @param resultRetriever result retriever
     * @param jobQueue        job queue
     * @param config          app config
     * @deprecated use the new default constructor:
     * {@link #WebScanner(IResultRetriever, IJobQueue, IConfig, IExceptionHandler, ICLOutput)}
     */
    public WebScanner(
            IResultRetriever resultRetriever,
            IJobQueue jobQueue,
            IConfig config
    ) {
        this(resultRetriever, jobQueue, config, null, null);
    }

    /**
     * Default constructor.
     *
     * @param resultRetriever  result retriever
     * @param jobQueue         job queue
     * @param config           app config
     * @param exceptionHandler exception handler
     */
    @Inject
    public WebScanner(
            IResultRetriever resultRetriever,
            IJobQueue jobQueue,
            IConfig config,
            IExceptionHandler exceptionHandler,
            ICLOutput iclOutput
    ) {
        this.resultRetriever = resultRetriever;
        this.jobQueue = jobQueue;
        this.config = config;
        this.exceptionHandler = exceptionHandler;
        this.iclOutput = iclOutput;
    }

    @Override
    public void afterStart() {
        super.afterStart();
        indexId.clear();
        indexUrl.clear();
        this.refreshRunnable = new WebScannerUrlRefresher(
                indexUrl, config.getConfig().urlRefreshTime()
        );
        this.refreshThread = new Thread(refreshRunnable);
        refreshThread.setDaemon(true);
        refreshThread.start();
    }

    @Override
    public Future<Result> submit(IJob job) {
        if (job == null) {
            ScannerException e = new ScannerException(
                    "WebScanner::submit job cannot be null"
            );
            if (exceptionHandler == null)
                throw new RuntimeComponentException(e);
            exceptionHandler.handle(e);
        }
        if (!(job instanceof WebJob)) {
            ScannerException e = new ScannerException(
                    "WebScanner::submit job must be of type WebJob"
            );
            if (exceptionHandler == null)
                throw new RuntimeComponentException(e);
            exceptionHandler.handle(e);
        }
        WebJob webJob = (WebJob) job;
        if (indexId.containsKey(webJob.getId())) {
            Future<Result> fr = indexId.get(webJob.getId());
            if (fr.isDone() || fr.isCancelled()) {
                try {
                    fr.get().setCached(true);
                } catch (InterruptedException | ExecutionException ignored) {
                    ;
                }
            }
            return fr;
        }
        if (indexUrl.containsKey(webJob.getUrl())) {
            Future<Result> fr = indexUrl.get(webJob.getUrl());
            if (fr.isDone() || fr.isCancelled()) {
                try {
                    fr.get().setCached(true);
                } catch (InterruptedException | ExecutionException ignored) {
                    ;
                }
            }
            return fr;
        }
        WebScannerCallable scanner = new WebScannerCallable(webJob, jobQueue, config);
        Future<Result> res = getPool().submit(scanner);
        indexId.put(webJob.getId(), res);
        indexUrl.put(webJob.getUrl(), res);
        getJobResults().put(job, res);
        resultRetriever.getStoreWebJobs().put(job, res);
        return res;
    }

    @Override
    public void main() {
        keepAlive();
    }

    @Override
    public void beforeEnd() {
        refreshRunnable.stop();
        super.beforeEnd();
    }
}
