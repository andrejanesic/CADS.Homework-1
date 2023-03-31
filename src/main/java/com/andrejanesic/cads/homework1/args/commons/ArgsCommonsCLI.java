package com.andrejanesic.cads.homework1.args.commons;

import com.andrejanesic.cads.homework1.args.IArgs;
import com.andrejanesic.cads.homework1.core.exceptions.ArgsException;
import org.apache.commons.cli.*;

import javax.inject.Inject;

/**
 * Argument parser using Apache Commons CLI.
 */
public class ArgsCommonsCLI extends IArgs {

    private CommandLine cmd;

    @Inject
    public ArgsCommonsCLI() {
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
            throw new ArgsException(e.getMessage());
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
