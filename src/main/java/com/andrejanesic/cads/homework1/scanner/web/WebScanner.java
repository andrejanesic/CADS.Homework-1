package com.andrejanesic.cads.homework1.scanner.web;

import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.RuntimeComponentException;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.job.result.Result;
import com.andrejanesic.cads.homework1.job.type.WebJob;
import com.andrejanesic.cads.homework1.scanner.IFileScanner;
import com.andrejanesic.cads.homework1.utils.LoopRunnable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Singleton
public class WebScanner extends IFileScanner {

    private final IJobQueue jobQueue;
    private final IConfig config;
    private Map<String, Future<Result>> indexId = new HashMap<>();
    private Map<String, Future<Result>> indexUrl = new HashMap<>();
    private Thread refreshThread;
    private LoopRunnable refreshRunnable;

    @Inject
    public WebScanner(IJobQueue jobQueue, IConfig config) {
        this.jobQueue = jobQueue;
        this.config = config;
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
        if (!(job instanceof WebJob webJob))
            throw new RuntimeComponentException("Invalid job type passed");
        if (indexId.containsKey(webJob.getId()) || indexUrl.containsKey(webJob.getUrl()))
            return null;
        WebScannerCallable scanner = new WebScannerCallable(webJob, jobQueue, config);
        Future<Result> res = getPool().submit(scanner);
        indexId.put(webJob.getId(), res);
        indexUrl.put(webJob.getUrl(), res);
        // TODO submit to result retriever
        return res;
    }

    @Override
    public void beforeEnd() {
        refreshRunnable.stop();
        super.beforeEnd();
    }
}
