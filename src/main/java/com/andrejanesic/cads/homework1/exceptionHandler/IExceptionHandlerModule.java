package com.andrejanesic.cads.homework1.exceptionHandler;

import com.andrejanesic.cads.homework1.exceptionHandler.impl.ExceptionHandler;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface IExceptionHandlerModule {

    @Binds
    @Singleton
    IExceptionHandler bindExceptionHandler(ExceptionHandler impl);
}
