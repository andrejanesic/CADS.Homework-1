package com.andrejanesic.cads.homework1.cli.input.commands;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;
import com.andrejanesic.cads.homework1.config.IConfig;
import com.andrejanesic.cads.homework1.core.exceptions.CLInputException;
import com.andrejanesic.cads.homework1.core.exceptions.RoutineException;
import com.andrejanesic.cads.homework1.core.routines.AddDirectoryRoutine;
import com.andrejanesic.cads.homework1.core.routines.AddWebsiteRoutine;
import com.andrejanesic.cads.homework1.core.routines.IRoutine;
import com.andrejanesic.cads.homework1.core.routines.RoutineManager;
import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;
import com.andrejanesic.cads.homework1.scanner.IWebScanner;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

@Singleton
public class CommandAdd extends ICommand {

    private static final String SYNTAX_ERROR = "Invalid syntax: add " +
            "(--dir|--web) (path|URL)";
    private final ICLOutput clOutput;
    private final IDirectoryCrawler directoryCrawler;
    private final IWebScanner webScanner;
    private final IConfig config;

    @Inject
    public CommandAdd(
            ICLOutput clOutput,
            IDirectoryCrawler directoryCrawler,
            IWebScanner webScanner,
            IConfig config
    ) {
        super();
        this.clOutput = clOutput;
        this.directoryCrawler = directoryCrawler;
        this.webScanner = webScanner;
        this.config = config;
        command("add");
        allowedOptions("web", "dir");
    }

    @Override
    public void exec() throws CLInputException {
        List<String> args = getCommandLine().getArgList();

        if (!getCommandLine().hasOption("web") &&
                !getCommandLine().hasOption("dir")) {
            throw new CLInputException(SYNTAX_ERROR);
        }

        if (args.size() != 2) {
            throw new CLInputException(SYNTAX_ERROR);
        }

        boolean dir = getCommandLine().hasOption("dir");
        String path = args.get(1);

        IRoutine routine;
        if (dir) {
            File f = new File(path);
            path = f.getAbsolutePath();
            routine = new AddDirectoryRoutine(
                    directoryCrawler,
                    path
            );
        } else {
            if (!Pattern.compile("^https?://.*$")
                    .matcher(path).matches()) {
                path = "http://" + path;
            }
            routine = new AddWebsiteRoutine(
                    webScanner,
                    config,
                    path
            );
        }

        RoutineManager rm = RoutineManager.getInstance();
        try {
            rm.addRoutine(routine);
        } catch (RoutineException e) {
            throw new CLInputException(e);
        }
    }
}
