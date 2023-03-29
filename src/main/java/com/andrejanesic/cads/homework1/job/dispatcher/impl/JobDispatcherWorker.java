package com.andrejanesic.cads.homework1.job.dispatcher.impl;

import com.andrejanesic.cads.homework1.core.exceptions.JobQueueException;
import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.queue.IJobQueue;
import com.andrejanesic.cads.homework1.utils.LoopRunnable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JobDispatcherWorker extends LoopRunnable {

    @NonNull
    private final IJobQueue jobQueue;

    @Override
    public void loop() {
        try {
            IJob job = jobQueue.dequeueJob();
            // TODO run job
        } catch (JobQueueException e) {
            // TODO handle exception
            throw new RuntimeException(e);
        }
    }

}
