package com.andrejanesic.cads.homework1.cli.commons;

import com.andrejanesic.cads.homework1.cli.commons.commands.CommandManager;
import com.andrejanesic.cads.homework1.core.exceptions.ComponentException;
import com.andrejanesic.cads.homework1.utils.LoopRunnable;
import org.apache.commons.cli.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Scanner;

/**
 * Listens for user input.
 */
@Singleton
public class InputReader extends LoopRunnable {

    private final Scanner scanner = new Scanner(System.in);
    private final CommandLineParser parser;
    private final Options options;
    private final CommandManager commandManager;

    @Inject
    public InputReader(CommandManager commandManager) {
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
    public void loop() throws ComponentException {
        // TODO replace out with custom class
        System.out.print("$ ");
        String input = scanner.nextLine();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, input.split("\\s+"));
        } catch (ParseException e) {
            // TODO report err
            return;
        }

        List<String> args = cmd.getArgList();
        if (args.size() < 1) {
            // TODO report err
            return;
        }

        // attempt to parse and execute each
        CommandLine finalCmd = cmd;
        commandManager.getCommands().forEach(comm -> {
            comm.setInput(input);
            comm.setCommandLine(finalCmd);
            comm.parse();
        });
    }
}
