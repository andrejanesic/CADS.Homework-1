package com.andrejanesic.cads.homework1.utils;

import com.andrejanesic.cads.homework1.core.exceptions.JobQueueException;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import lombok.Getter;
import lombok.NonNull;

import java.util.concurrent.Callable;

@Getter
public class MockJobEnqueuerCallable implements Callable<IJob> {

    @NonNull
    private final IJob job;
    @NonNull
    private final IJobQueue jobQueue;
    @NonNull
    private final Object lock;

    public MockJobEnqueuerCallable(IJob job, IJobQueue jobQueue, Object lock) {
        this.job = job;
        this.jobQueue = jobQueue;
        this.lock = lock;
    }

    @Override
    public IJob call() throws Exception {
        try {
            jobQueue.enqueueJob(job);
        } catch (JobQueueException e) {
            throw new RuntimeException(e);
        }
        return job;
    }
}
