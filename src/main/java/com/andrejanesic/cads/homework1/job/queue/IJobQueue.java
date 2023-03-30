package com.andrejanesic.cads.homework1.job.queue;

import com.andrejanesic.cads.homework1.core.ThreadedComponent;
import com.andrejanesic.cads.homework1.core.exceptions.JobQueueException;
import com.andrejanesic.cads.homework1.job.IJob;

import java.util.List;

/**
 * Synchronous queue of jobs.
 */
public abstract class IJobQueue extends ThreadedComponent {

    /**
     * Adds a new job to the queue.
     *
     * @param job {@link IJob} to add.
     * @throws JobQueueException In case of runtime error.
     */
    public abstract void enqueueJob(IJob job) throws JobQueueException;

    /**
     * Fetches the latest job from the queue.
     *
     * @return {@link IJob}
     * @throws JobQueueException In case of runtime error.
     */
    public abstract IJob dequeueJob() throws JobQueueException;

    /**
     * Returns the current state of the queue.
     *
     * @return List of jobs.
     */
    public abstract List<IJob> getQueue();
}
