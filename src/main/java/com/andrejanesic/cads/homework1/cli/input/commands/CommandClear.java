package com.andrejanesic.cads.homework1.cli.input.commands;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CommandClear extends ICommand {

    @Inject
    public CommandClear() {
        super();
        command("clear");
    }

    @Override
    public void exec() {

    }
}
