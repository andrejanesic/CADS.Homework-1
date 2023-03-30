package com.andrejanesic.cads.homework1.cli.commons.commands;

import com.andrejanesic.cads.homework1.cli.commons.OutputWriter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class CommandError extends ICommand {

    private OutputWriter out;

    @Inject
    public CommandError(OutputWriter out) {
        this.out = out;
    }

    @Override
    public void exec() {
        List<String> args = getCommandLine().getArgList();
        if (args.size() < 1) return;
        out.log(
                OutputWriter.MessageType.INFO,
                "The command \"" + args.get(0) + "\" couldn't be recognized."
        );
    }
}
