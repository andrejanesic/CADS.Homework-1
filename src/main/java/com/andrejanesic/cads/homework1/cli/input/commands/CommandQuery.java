package com.andrejanesic.cads.homework1.cli.input.commands;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CommandQuery extends ICommand {

    @Inject
    public CommandQuery() {
        super();
        command("query");
    }

    @Override
    public void exec() {

    }
}
