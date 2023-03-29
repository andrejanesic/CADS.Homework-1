package com.andrejanesic.cads.homework1.cli.commons.commands;

public class CommandStop extends ICommand {

    public CommandStop() {
        super();
        command("stop");
        minArgs(1);
        maxArgs(1);
    }

    @Override
    public void exec() {

    }
}
