package com.andrejanesic.cads.homework1.scanner.file;

import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.RuntimeComponentException;
import com.andrejanesic.cads.homework1.core.exceptions.UnexpectedRuntimeComponentException;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.job.type.WebJob;
import com.andrejanesic.cads.homework1.scanner.IFileScanner;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class FileScanner extends IFileScanner {

    private final IJobQueue jobQueue;
    private final IConfig config;
    private Map<String, FileScannerCallable> indexId = new HashMap<>();
    private Map<String, FileScannerCallable> indexUrl = new HashMap<>();
    private Long lastRefresh;

    @Inject
    public FileScanner(IJobQueue jobQueue, IConfig config) {
        this.jobQueue = jobQueue;
        this.config = config;
    }

    @Override
    public void init() {
        super.init();

        indexId.clear();
        indexUrl.clear();
        lastRefresh = System.currentTimeMillis();
        new Thread(() -> {
            // TODO check if urls refreshed as a group, or each individually after timeout
            try {
                Thread.sleep(config.getConfig().urlRefreshTime());
            } catch (InterruptedException e) {
                throw new UnexpectedRuntimeComponentException(e);
            }
            long curr = System.currentTimeMillis();
            if (curr > lastRefresh) {
                lastRefresh = curr;
                indexUrl.clear();
            }
        });
    }

    @Override
    public void submit(IJob job) {
        if (!(job instanceof WebJob webJob))
            throw new RuntimeComponentException("Invalid job type passed");
        if (indexId.containsKey(webJob.getId()) || indexUrl.containsKey(webJob.getUrl()))
            return;
        FileScannerCallable scanner = new FileScannerCallable(webJob, jobQueue, config);
        indexId.put(webJob.getId(), scanner);
        indexUrl.put(webJob.getUrl(), scanner);
    }

}
