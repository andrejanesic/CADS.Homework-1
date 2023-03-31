package com.andrejanesic.cads.homework1.cli.input.commands;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class CommandError extends ICommand {

    private ICLOutput iclOutput;

    @Inject
    public CommandError(ICLOutput iclOutput) {
        this.iclOutput = iclOutput;
    }

    @Override
    public void exec() {
        List<String> args = getCommandLine().getArgList();
        if (args.size() < 1) return;
        iclOutput.error(
                "The command \"" + args.get(0) + "\" couldn't be recognized."
        );
    }
}
