package com.andrejanesic.cads.homework1.job.dispatcher.impl;

import com.andrejanesic.cads.homework1.job.dispatcher.IJobDispatcher;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;

import javax.inject.Inject;

public class JobDispatcher extends IJobDispatcher {

    private final IJobQueue jobQueue;
    private Thread singleThread;
    private JobDispatcherWorker jobDispatcherWorker;

    @Inject
    public JobDispatcher(IJobQueue jobQueue) {
        this.jobQueue = jobQueue;
    }

    @Override
    public void afterStart() {
        super.afterStart();
        if (singleThread != null) {
            return;
        }

        jobDispatcherWorker = new JobDispatcherWorker(jobQueue);
        singleThread = new Thread(jobDispatcherWorker);
        singleThread.start();
    }

    @Override
    public void beforeEnd() {
        jobDispatcherWorker.stop();
        super.beforeEnd();
    }
}
