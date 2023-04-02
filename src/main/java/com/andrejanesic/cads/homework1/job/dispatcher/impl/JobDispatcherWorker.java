package com.andrejanesic.cads.homework1.job.dispatcher.impl;

import com.andrejanesic.cads.homework1.core.exceptions.JobQueueException;
import com.andrejanesic.cads.homework1.core.exceptions.RuntimeComponentException;
import com.andrejanesic.cads.homework1.exceptionHandler.IExceptionHandler;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.dispatcher.IJobDispatcher;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.scanner.IFileScanner;
import com.andrejanesic.cads.homework1.scanner.IWebScanner;
import com.andrejanesic.cads.homework1.utils.LoopRunnable;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JobDispatcherWorker extends LoopRunnable {

    private final IJobQueue jobQueue;
    private final IExceptionHandler exceptionHandler;
    private final IWebScanner webScanner;
    private final IFileScanner fileScanner;
    private final IJobDispatcher jobDispatcher;

    @Override
    public void loop() {
        try {
            if (jobQueue == null) {
                JobQueueException e = new JobQueueException(
                        "JobDispatcherWorker::loop jobQueue cannot be null"
                );
                if (exceptionHandler == null) {
                    throw new RuntimeComponentException(e);
                }
                exceptionHandler.handle(e);
            }
            assert jobQueue != null;
            IJob job = jobQueue.dequeueJob();
            if (job == null) return;
            if (!isAlive()) return;

            if (job.getType() == null) {
                JobQueueException e = new JobQueueException(
                        "JobDispatcherWorker::loop job.getType() cannot be null"
                );
                if (exceptionHandler == null) {
                    throw new RuntimeComponentException(e);
                }
                exceptionHandler.handle(e);
            }

            switch (job.getType()) {
                case WEB -> {
                    if (webScanner == null) {
                        JobQueueException e = new JobQueueException(
                                "JobDispatcherWorker::loop webScanner cannot " +
                                        "be null"
                        );
                        if (exceptionHandler == null) {
                            throw new RuntimeComponentException(e);
                        }
                        exceptionHandler.handle(e);
                    }

                    assert webScanner != null;
                    webScanner.submit(job);
                }
                case FILE -> {
                    if (fileScanner == null) {
                        JobQueueException e = new JobQueueException(
                                "JobDispatcherWorker::loop fileScanner cannot" +
                                        " be null"
                        );
                        if (exceptionHandler == null) {
                            throw new RuntimeComponentException(e);
                        }
                        exceptionHandler.handle(e);
                    }

                    fileScanner.submit(job);
                }
                case STOP -> {

                    if (fileScanner == null) {
                        JobQueueException e = new JobQueueException(
                                "JobDispatcherWorker::loop fileScanner cannot" +
                                        " be null"
                        );
                        if (exceptionHandler == null) {
                            throw new RuntimeComponentException(e);
                        }
                        exceptionHandler.handle(e);
                    }

                    fileScanner.submit(job);

                    if (webScanner == null) {
                        JobQueueException e = new JobQueueException(
                                "JobDispatcherWorker::loop webScanner cannot " +
                                        "be null"
                        );
                        if (exceptionHandler == null) {
                            throw new RuntimeComponentException(e);
                        }
                        exceptionHandler.handle(e);
                    }

                    webScanner.submit(job);

                    this.stop();
                }
                default -> {
                    JobQueueException e = new JobQueueException(
                            "JobDispatcherWorker::loop unknown job.getType():" +
                                    " " + job.getType()
                    );
                    if (exceptionHandler == null) {
                        throw new RuntimeComponentException(e);
                    }
                    exceptionHandler.handle(e);
                }
            }
        } catch (JobQueueException e) {
            if (exceptionHandler == null) {
                throw new RuntimeComponentException(e);
            }
            exceptionHandler.handle(e);
        }
    }

}
