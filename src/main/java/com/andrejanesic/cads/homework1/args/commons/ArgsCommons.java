package com.andrejanesic.cads.homework1.args.commons;

import com.andrejanesic.cads.homework1.args.IArgs;
import com.andrejanesic.cads.homework1.core.exceptions.ArgsException;
import com.andrejanesic.cads.homework1.exceptionHandler.IExceptionHandler;
import org.apache.commons.cli.*;

import javax.inject.Inject;

/**
 * Argument parser using Apache Commons CLI.
 */
public class ArgsCommons extends IArgs {

    private final IExceptionHandler exceptionHandler;
    private CommandLine cmd;

    /**
     * @deprecated use new default constructor: {@link #ArgsCommons(IExceptionHandler)}
     */
    public ArgsCommons() {
        this(null);
    }

    @Inject
    public ArgsCommons(IExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void parse(String[] args) throws ArgsException {
        // Set options
        Options options = new Options();

        Option configPath = Option.builder("c")
                .hasArg()
                .numberOfArgs(1)
                .longOpt("config")
                .argName("file path")
                .desc("Configuration file path")
                .type(String.class)
                .build();
        options.addOption(configPath);

        // Parse
        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            if (exceptionHandler == null)
                throw new ArgsException(e.getMessage());
            exceptionHandler.handle(e);
        }
    }

    @Override
    public String configSource() {
        if (cmd == null || (!cmd.hasOption("c"))) return super.configSource();
        return cmd.getOptionValue("c");
    }

    @Override
    public void afterStart() {

    }

    @Override
    public void main() {

    }

    @Override
    public void beforeEnd() {

    }
}
