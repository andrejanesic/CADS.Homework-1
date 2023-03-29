package com.andrejanesic.cads.homework1.args;

import com.andrejanesic.cads.homework1.args.commons.ArgsCommonsCLI;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface IArgsModule {

    @Binds
    @Singleton
    IArgs bindArgs(ArgsCommonsCLI impl);

}
