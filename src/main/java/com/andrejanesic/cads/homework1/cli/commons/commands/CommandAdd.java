package com.andrejanesic.cads.homework1.cli.commons.commands;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CommandAdd extends ICommand {

    @Inject
    public CommandAdd() {
        super();
        command("add");
        minArgs(2);
    }

    @Override
    public void exec() {

    }
}
