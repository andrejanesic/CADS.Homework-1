package com.andrejanesic.cads.homework1.cli.input.commands;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CommandClear extends ICommand {

    @Inject
    public CommandClear() {
        super();
        command("clear");
        minArgs(1);
        maxArgs(1);
    }

    @Override
    public void exec() {

    }
}
