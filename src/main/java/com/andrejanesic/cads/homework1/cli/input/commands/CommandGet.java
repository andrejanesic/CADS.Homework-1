package com.andrejanesic.cads.homework1.cli.input.commands;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CommandGet extends ICommand {

    @Inject
    public CommandGet() {
        super();
        command("get");
    }

    @Override
    public void exec() {

    }
}
