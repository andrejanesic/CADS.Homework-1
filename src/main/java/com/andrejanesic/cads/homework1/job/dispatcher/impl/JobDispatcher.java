package com.andrejanesic.cads.homework1.job.dispatcher.impl;

import com.andrejanesic.cads.homework1.exceptionHandler.IExceptionHandler;
import com.andrejanesic.cads.homework1.job.dispatcher.IJobDispatcher;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.scanner.IFileScanner;
import com.andrejanesic.cads.homework1.scanner.IWebScanner;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JobDispatcher extends IJobDispatcher {

    private final IJobQueue jobQueue;
    private final IExceptionHandler exceptionHandler;
    private final IWebScanner webScanner;
    private final IFileScanner fileScanner;
    private JobDispatcherWorker jobDispatcherWorker;

    /**
     * @param jobQueue job queue
     * @deprecated use the new default constructor:
     * {@link #JobDispatcher(IJobQueue, IExceptionHandler, IWebScanner, IFileScanner)}
     */
    public JobDispatcher(IJobQueue jobQueue) {
        this(jobQueue, null, null, null);
    }

    /**
     * Default constructor.
     *
     * @param jobQueue         job queue
     * @param exceptionHandler exception handler
     */
    @Inject
    public JobDispatcher(
            IJobQueue jobQueue,
            IExceptionHandler exceptionHandler,
            IWebScanner webScanner,
            IFileScanner fileScanner
    ) {
        this.jobQueue = jobQueue;
        this.exceptionHandler = exceptionHandler;
        this.webScanner = webScanner;
        this.fileScanner = fileScanner;
    }

    @Override
    public void afterStart() {
        if (jobDispatcherWorker != null) {
            return;
        }
        jobDispatcherWorker = new JobDispatcherWorker(
                jobQueue,
                exceptionHandler,
                webScanner,
                fileScanner,
                this
        );
        startNewThread(jobDispatcherWorker);
    }

    @Override
    public void main() {
        keepAlive();
    }
}
