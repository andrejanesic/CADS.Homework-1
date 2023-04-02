package com.andrejanesic.cads.homework1.cli.input.commands;

import com.andrejanesic.cads.homework1.cli.output.ICLOutput;
import com.andrejanesic.cads.homework1.core.exceptions.CLInputException;
import com.andrejanesic.cads.homework1.core.exceptions.RoutineException;
import com.andrejanesic.cads.homework1.core.routines.IRoutine;
import com.andrejanesic.cads.homework1.core.routines.QueryRoutine;
import com.andrejanesic.cads.homework1.core.routines.RoutineManager;
import com.andrejanesic.cads.homework1.job.JobType;
import com.andrejanesic.cads.homework1.job.query.Query;
import com.andrejanesic.cads.homework1.resultRetriever.IResultRetriever;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Singleton
public class CommandQuery extends ICommand {

    private static final String SYNTAX_ERROR_QUERY = "Invalid syntax: query " +
            "(--file|--web) [--regex] (path|url|summary)";
    private static final String SYNTAX_ERROR_GET = "Invalid syntax: get" +
            "(--file|--web) [--regex] (path|url|summary)";
    private final IResultRetriever resultRetriever;
    private final ICLOutput iclOutput;

    @Inject
    public CommandQuery(
            IResultRetriever resultRetriever,
            ICLOutput iclOutput
    ) {
        super();
        this.resultRetriever = resultRetriever;
        this.iclOutput = iclOutput;
        command("query", "get");
        allowedOptions("web", "file", "regex");
    }

    @Override
    public void exec() throws CLInputException {
        List<String> args = getCommandLine().getArgList();
        boolean query = args.get(0).equalsIgnoreCase("query");
        String syntErr = query ? SYNTAX_ERROR_QUERY : SYNTAX_ERROR_GET;

        if (!getCommandLine().hasOption("web") &&
                !getCommandLine().hasOption("file")) {
            throw new CLInputException(syntErr);
        }

        if (args.size() != 2) {
            throw new CLInputException(syntErr);
        }

        boolean dir = getCommandLine().hasOption("file");
        boolean regex = getCommandLine().hasOption("regex");
        String path = args.get(1);

        if (!regex) {
            if (path.equalsIgnoreCase("summary")) {
                path = "^.*$";
            } else {
                if (dir) {
                    File f = new File(path);
                    String absPath = f.getAbsolutePath();
                    if (!absPath.endsWith(File.separator)) {
                        absPath = absPath + File.separator;
                    }
                    absPath = absPath.replaceAll(
                            "\\\\", "\\\\\\\\");
                    absPath = absPath + ".*";
                    path = "^" + absPath + "$";
                } else {
                    if (!Pattern.compile("^https?://.*$")
                            .matcher(path).matches()) {
                        path = path.replaceAll("\\.", "\\\\.");
                        path = "^https?://" + path + ".*$";
                    }
                }
            }
        }

        Pattern pattern;
        try {
            pattern = Pattern.compile(path);
        } catch (PatternSyntaxException e) {
            throw new CLInputException(e.getMessage());
        }
        Query q = Query.builder()
                .uri(pattern)
                .type(dir ? JobType.FILE : JobType.WEB)
                .wait(!query)
                .build();
        IRoutine routine = new QueryRoutine(
                resultRetriever,
                iclOutput,
                q
        );

        RoutineManager rm = RoutineManager.getInstance();
        try {
            rm.addRoutine(routine);
        } catch (RoutineException e) {
            throw new CLInputException(e);
        }
    }
}
