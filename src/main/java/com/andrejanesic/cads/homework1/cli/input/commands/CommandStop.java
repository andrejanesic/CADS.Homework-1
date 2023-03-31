package com.andrejanesic.cads.homework1.cli.input.commands;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CommandStop extends ICommand {

    @Inject
    public CommandStop() {
        super();
        command("stop", "exit");
        minArgs(1);
        maxArgs(1);
    }

    @Override
    public void exec() {

    }
}
