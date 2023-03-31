package com.andrejanesic.cads.homework1.resultRetriever;

import com.andrejanesic.cads.homework1.resultRetriever.impl.ResultRetriever;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface IResultRetrieverModule {

    @Binds
    @Singleton
    IResultRetriever bindResultRetriever(ResultRetriever impl);
}
