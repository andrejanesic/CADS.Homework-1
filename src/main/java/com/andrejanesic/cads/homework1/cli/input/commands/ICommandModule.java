package com.andrejanesic.cads.homework1.cli.input.commands;

import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface ICommandModule {

    @Binds
    @Singleton
    ICommandManager bindCommandManager(CommandManager impl);

}
