package com.andrejanesic.cads.homework1.cli.output;

import com.andrejanesic.cads.homework1.cli.output.impl.CLOutput;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface ICLOutputModule {

    @Binds
    @Singleton
    ICLOutput bindICLOutput(CLOutput impl);

}
