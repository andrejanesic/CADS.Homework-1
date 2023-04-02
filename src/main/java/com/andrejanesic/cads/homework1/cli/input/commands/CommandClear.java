package com.andrejanesic.cads.homework1.cli.input.commands;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;
import com.andrejanesic.cads.homework1.core.exceptions.CLInputException;
import com.andrejanesic.cads.homework1.core.exceptions.RoutineException;
import com.andrejanesic.cads.homework1.core.routines.ClearRoutine;
import com.andrejanesic.cads.homework1.core.routines.IRoutine;
import com.andrejanesic.cads.homework1.core.routines.RoutineManager;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class CommandClear extends ICommand {

    private static final String SYNTAX_ERROR = "Invalid syntax: clear " +
            "(--file|--web)";

    private final ICLOutput iclOutput;
    private final IResultRetriever resultRetriever;

    @Inject
    public CommandClear(
            ICLOutput iclOutput,
            IResultRetriever resultRetriever
    ) {
        super();
        this.iclOutput = iclOutput;
        this.resultRetriever = resultRetriever;
        command("clear");
        allowedOptions("web", "file");
    }

    @Override
    public void exec() throws CLInputException {
        List<String> args = getCommandLine().getArgList();

        if (!getCommandLine().hasOption("web") &&
                !getCommandLine().hasOption("file")) {
            throw new CLInputException(SYNTAX_ERROR);
        }

        if (args.size() != 1) {
            throw new CLInputException(SYNTAX_ERROR);
        }

        boolean file = getCommandLine().hasOption("file");

        IRoutine routine = new ClearRoutine(
                iclOutput,
                resultRetriever,
                file
        );

        RoutineManager rm = RoutineManager.getInstance();
        try {
            rm.addRoutine(routine);
        } catch (RoutineException e) {
            throw new CLInputException(e);
        }
    }
}
