package com.andrejanesic.cads.homework1.config;

import com.andrejanesic.cads.homework1.config.cfg4j.CFG4JLoader;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public interface IConfigModule {

    @Binds
    @Singleton
    IConfig bindConfig(CFG4JLoader impl);

}
