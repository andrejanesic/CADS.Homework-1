package com.andrejanesic.cads.homework1.job.dispatcher;

import com.andrejanesic.cads.homework1.job.dispatcher.impl.JobDispatcher;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface IJobDispatcherModule {

    @Binds
    @Singleton
    IJobDispatcher bindJobDispatcher(JobDispatcher impl);

}
