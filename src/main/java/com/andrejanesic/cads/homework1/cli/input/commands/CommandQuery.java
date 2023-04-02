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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Singleton
public class CommandQuery extends ICommand {

    private static final String SYNTAX_ERROR_QUERY = "Invalid syntax: query " +
            "(--file|--web) [--regex] (<path>|<url>|summary)";
    private static final String SYNTAX_ERROR_GET = "Invalid syntax: get" +
            "(--file|--web) [--regex] (<path>|<url>|summary|all)";
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

        Set<Pattern> uris = new HashSet<>();

        if (!regex) {
            if (path.equalsIgnoreCase("all")) {
                path = "^.*$";
            } else if (path.equalsIgnoreCase("summary")) {
                if (dir) {
                    resultRetriever.getIndexedDirectories().forEach(
                            (dirPath, ignore) -> {
                                if (!dirPath.endsWith(File.separator)) {
                                    dirPath = dirPath + File.separator + "?";
                                }
                                if (!dirPath.startsWith(File.separator)) {
                                    dirPath = File.separator + dirPath;
                                }
                                dirPath = dirPath.replaceAll(
                                        "\\\\", "\\\\\\\\");
                                dirPath = ".*" + dirPath + ".*";
                                uris.add(Pattern.compile(dirPath));
                            });
                } else {
                    final Pattern webPat =
                            Pattern.compile("^https?://.*$");
                    resultRetriever.getIndexedWebsites().forEach(
                            (webRoot, ignore) -> {
                                if (!webPat.matcher(webRoot).matches()) {
                                    webRoot = webRoot.replaceAll(
                                            "\\.",
                                            "\\\\."
                                    );
                                    webRoot = "^https?://" + webRoot + ".*$";
                                }
                                uris.add(Pattern.compile(webRoot));
                            });
                }
            } else {
                if (dir) {
                    if (!path.endsWith(File.separator)) {
                        path = path + File.separator + "?";
                    }
                    if (!path.startsWith(File.separator)) {
                        path = File.separator + path;
                    }
                    path = path.replaceAll(
                            "\\\\", "\\\\\\\\");
                    path = ".*" + path + ".*";
                } else {
                    if (!Pattern.compile("^https?://.*$")
                            .matcher(path).matches()) {
                        path = path.replaceAll("\\.", "\\\\.");
                        path = "^https?://" + path + ".*$";
                    }
                }
            }
        }

        Pattern pattern = null;
        try {
            if (path != null)
                pattern = Pattern.compile(path);
        } catch (PatternSyntaxException e) {
            throw new CLInputException(e.getMessage());
        }
        Query.QueryBuilder qb = Query.builder()
                .type(dir ? JobType.FILE : JobType.WEB)
                .wait(!query);
        if (pattern != null) {
            qb.uri(pattern);
        }
        if (uris.size() != 0) {
            qb.uris(uris);
        }
        IRoutine routine = new QueryRoutine(
                resultRetriever,
                iclOutput,
                qb.build()
        );

        RoutineManager rm = RoutineManager.getInstance();
        try {
            rm.addRoutine(routine);
        } catch (RoutineException e) {
            throw new CLInputException(e);
        }
    }
}
