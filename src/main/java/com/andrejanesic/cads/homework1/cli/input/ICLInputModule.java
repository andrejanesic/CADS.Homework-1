package com.andrejanesic.cads.homework1.cli.input;

import com.andrejanesic.cads.homework1.cli.input.commons.CLInputCommons;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface ICLInputModule {

    @Binds
    @Singleton
    ICLInput bindICLInput(CLInputCommons impl);

}
