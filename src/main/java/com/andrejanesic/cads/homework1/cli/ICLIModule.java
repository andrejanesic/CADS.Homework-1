package com.andrejanesic.cads.homework1.cli;

import com.andrejanesic.cads.homework1.cli.commons.CLICommons;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface ICLIModule {

    @Binds
    @Singleton
    ICLI bindICLI(CLICommons impl);
}
