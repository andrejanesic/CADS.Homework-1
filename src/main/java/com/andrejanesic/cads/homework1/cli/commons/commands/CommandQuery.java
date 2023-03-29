package com.andrejanesic.cads.homework1.cli.commons.commands;

public class CommandQuery extends ICommand {

    public CommandQuery() {
        super();
        command("query");
        minArgs(2);
    }

    @Override
    public void exec() {

    }
}
