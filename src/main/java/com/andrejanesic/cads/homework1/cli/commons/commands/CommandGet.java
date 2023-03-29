package com.andrejanesic.cads.homework1.cli.commons.commands;

public class CommandGet extends ICommand {

    public CommandGet() {
        super();
        command("get");
        minArgs(2);
    }

    @Override
    public void exec() {

    }
}
