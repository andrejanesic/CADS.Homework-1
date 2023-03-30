package com.andrejanesic.cads.homework1.cli.commons.commands;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CommandGet extends ICommand {

    @Inject
    public CommandGet() {
        super();
        command("get");
        minArgs(2);
    }

    @Override
    public void exec() {

    }
}
