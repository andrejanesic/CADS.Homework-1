package com.andrejanesic.cads.homework1.cli.input.commands;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;
import com.andrejanesic.cads.homework1.core.exceptions.CLInputException;
import com.andrejanesic.cads.homework1.core.exceptions.RoutineException;
import com.andrejanesic.cads.homework1.core.routines.AddDirectoryRoutine;
import com.andrejanesic.cads.homework1.core.routines.IRoutine;
import com.andrejanesic.cads.homework1.core.routines.RoutineManager;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class CommandAdd extends ICommand {

    private static final String SYNTAX_ERROR = "Invalid syntax: add " +
            "(--dir|--web) (path|URL)";
    private final ICLOutput clOutput;
    private final IDirectoryCrawler directoryCrawler;

    @Inject
    public CommandAdd(
            ICLOutput clOutput,
            IDirectoryCrawler directoryCrawler
    ) {
        super();
        this.clOutput = clOutput;
        this.directoryCrawler = directoryCrawler;
        command("add");
    }

    @Override
    public void exec() throws CLInputException {
        List<String> args = getCommandLine().getArgList();

        if (!getCommandLine().hasOption("web") &&
                !getCommandLine().hasOption("dir")) {
            throw new CLInputException(SYNTAX_ERROR);
        }

        if (args.size() < 2) {
            throw new CLInputException(SYNTAX_ERROR);
        }

        boolean dir = getCommandLine().hasOption("dir");
        String path = args.get(1);

        IRoutine routine = new AddDirectoryRoutine(
                clOutput,
                directoryCrawler,
                path
        );

        RoutineManager rm = RoutineManager.getInstance();
        try {
            rm.addRoutine(routine);
        } catch (RoutineException e) {
            throw new CLInputException(e);
        }

    }
}
