package com.andrejanesic.cads.homework1.cli.commons.commands;

public class CommandClear extends ICommand {

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
