package com.andrejanesic.cads.homework1.job.queue;

import com.andrejanesic.cads.homework1.job.queue.impl.JobQueue;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class IJobQueueModule {

    @Provides
    @Singleton
    IJobQueue provideJobQueue() {
        return new JobQueue(0);
    }

}
