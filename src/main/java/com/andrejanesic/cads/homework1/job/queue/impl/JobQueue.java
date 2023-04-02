package com.andrejanesic.cads.homework1.job.queue.impl;

import com.andrejanesic.cads.homework1.core.exceptions.JobQueueException;
import com.andrejanesic.cads.homework1.exceptionHandler.IExceptionHandler;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

@Singleton
public class JobQueue extends IJobQueue {

    private final Deque<IJob> blockingDeque = new LinkedBlockingDeque<>();

    /**
     * Maximum size of the queue. If maxSize < 1, no max size imposed.
     */
    @Getter
    private final int maxSize;

    private final IExceptionHandler exceptionHandler;

    /**
     * @param maxSize max queue size
     * @deprecated use the new default constructor:
     * {@link #JobQueue(int, IExceptionHandler)}
     */
    public JobQueue(int maxSize) {
        this(maxSize, null);
    }

    /**
     * @deprecated use the new default constructor:
     * {@link #JobQueue(int, IExceptionHandler)}
     */
    public JobQueue() {
        this(-1, null);
    }

    /**
     * Default constructor.
     *
     * @param maxSize          max queue size
     * @param exceptionHandler exception handler
     */
    @Inject
    public JobQueue(int maxSize, IExceptionHandler exceptionHandler) {
        this.maxSize = maxSize;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public synchronized void enqueueJob(IJob job) throws JobQueueException {
        while (maxSize > 0 && blockingDeque.size() >= maxSize) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                JobQueueException ex = new JobQueueException(e);
                if (exceptionHandler == null)
                    throw ex;
                exceptionHandler.handle(ex);
            }
        }

        blockingDeque.addFirst(job);
        this.notifyAll();
    }

    @Override
    public synchronized IJob dequeueJob() throws JobQueueException {
        while (blockingDeque.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                JobQueueException ex = new JobQueueException(e);
                if (exceptionHandler == null)
                    throw ex;
                exceptionHandler.handle(ex);
            }
        }

        this.notifyAll();
        return blockingDeque.pollLast();
    }

    @Override
    public List<IJob> getQueue() {
        return new ArrayList<>(blockingDeque);
    }

    @Override
    public void afterStart() {

    }

    @Override
    public void main() {

    }

    @Override
    public void beforeEnd() {

    }
}
