package com.andrejanesic.cads.homework1.cli.input.commons;

import com.andrejanesic.cads.homework1.cli.input.ICLInput;
import com.andrejanesic.cads.homework1.cli.input.commands.ICommand;
import com.andrejanesic.cads.homework1.cli.input.commands.ICommandManager;
import com.andrejanesic.cads.homework1.core.exceptions.CLInputException;
import com.andrejanesic.cads.homework1.exceptionHandler.IExceptionHandler;
import org.apache.commons.cli.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Scanner;

public class CLInputCommons extends ICLInput {

    private final Scanner scanner = new Scanner(System.in);
    private final CommandLineParser parser;
    private final Options options;
    private final ICommandManager commandManager;
    private final IExceptionHandler exceptionHandler;

    /**
     * @param commandManager command manager
     * @deprecated use the new default constructor:
     * {@link #CLInputCommons(IExceptionHandler, ICommandManager)}
     */
    public CLInputCommons(
            ICommandManager commandManager
    ) {
        this(null, commandManager);
    }

    /**
     * Default constructor.
     *
     * @param exceptionHandler exception handler
     * @param commandManager   command manager
     */
    @Inject
    public CLInputCommons(
            IExceptionHandler exceptionHandler,
            ICommandManager commandManager
    ) {
        this.exceptionHandler = exceptionHandler;
        this.commandManager = commandManager;

        options = new Options();

        Option optionWeb = Option.builder("w")
                .hasArg(false)
                .longOpt("web")
                .build();
        options.addOption(optionWeb);

        Option optionDir = Option.builder("d")
                .hasArg(false)
                .longOpt("dir")
                .build();
        options.addOption(optionDir);

        Option optionFile = Option.builder("f")
                .hasArg(false)
                .longOpt("file")
                .build();
        options.addOption(optionFile);

        this.parser = new DefaultParser();
    }

    @Override
    public void afterStart() {

    }

    @Override
    public void main() {
        synchronized (getKeepAliveLock()) {
            while (isKeepAlive()) {
                String input = scanner.nextLine();
                CommandLine cmd = null;
                try {
                    cmd = parser.parse(options, input.split("\\s+"));
                } catch (ParseException e) {
                    if (exceptionHandler == null)
                        throw new RuntimeException(e);
                    exceptionHandler.handle(e);
                    return;
                }

                List<String> args = cmd.getArgList();
                if (args.size() < 1) {
                    String err = "Syntax error: empty input";
                    if (exceptionHandler == null)
                        throw new RuntimeException(
                                new CLInputException(err)
                        );
                    exceptionHandler.handle(
                            new CLInputException(err)
                    );
                    return;
                }

                // attempt to parse and execute each
                boolean recognized = false;
                for (ICommand comm : commandManager.getCommands()) {
                    if (recognized) break;
                    comm.setInput(input);
                    comm.setCommandLine(cmd);
                    try {
                        comm.parse();
                    } catch (CLInputException e) {
                        if (exceptionHandler == null)
                            throw new RuntimeException(e);
                        exceptionHandler.handle(e);
                    }
                    if (comm.isRecognized())
                        recognized = true;
                }
                if (!recognized) {
                    CLInputException e = new CLInputException(
                            "Command \"" + input + "\" not recognized"
                    );
                    if (exceptionHandler == null)
                        throw new RuntimeException(e);
                    exceptionHandler.handle(e);
                }
            }
        }
    }

    @Override
    public void beforeEnd() {

    }
}
