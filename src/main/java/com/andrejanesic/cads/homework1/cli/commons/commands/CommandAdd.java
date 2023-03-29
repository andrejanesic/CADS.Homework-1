package com.andrejanesic.cads.homework1.cli.commons.commands;

public class CommandAdd extends ICommand {

    public CommandAdd() {
        super();
        command("add");
        minArgs(2);
    }

    @Override
    public void exec() {

    }
}
