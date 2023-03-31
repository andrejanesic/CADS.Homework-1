package com.andrejanesic.cads.homework1.cli.input.commons.commands;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.cli.CommandLine;

import java.util.List;

/**
 * Used for parsing user args input.
 */
public abstract class ICommand {

    public ICommand() {
    }

    /**
     * User input, raw.
     */
    @Getter
    @Setter
    private String input;

    /**
     * Command line.
     */
    @Getter
    @Setter
    @NonNull
    private CommandLine commandLine;

    private boolean minArgsEnable = false;
    private int minArgs = -1;

    private boolean maxArgsEnable = false;
    private int maxArgs = -1;

    private boolean commandEnable = false;
    private String command = null;

    /**
     * Word that activates this command.
     *
     * @param command Word that activates this command.
     * @return Self.
     */
    ICommand command(String command) {
        this.commandEnable = true;
        this.command = command;
        return this;
    }

    /**
     * Minimum number of arguments required for this command.
     *
     * @param minArgs Minimum number of arguments required for this command.
     * @return Self.
     */
    ICommand minArgs(int minArgs) {
        minArgsEnable = true;
        this.minArgs = minArgs;
        return this;
    }

    /**
     * Maximum number of arguments required for this command (inclusive).
     *
     * @param maxArgs Maximum number of arguments required for this command (inclusive).
     * @return Self.
     */
    ICommand maxArgs(int maxArgs) {
        maxArgsEnable = true;
        this.maxArgs = maxArgs;
        return this;
    }

    /**
     * Verifies the user input against this command's rules and runs {@link #exec()} if command applicable.
     */
    public void parse() {
        List<String> args = commandLine.getArgList();

        if (commandEnable) {
            if (!minArgsEnable || minArgs < 1) {
                minArgsEnable = true;
                minArgs = 1;
            }

            if (args.size() < minArgs)
                return;
            if (!args.get(0).equalsIgnoreCase(command))
                return;
        }

        if (minArgsEnable && minArgs > 0) {
            if (args.size() < minArgs)
                return;
        }

        if (maxArgsEnable && maxArgs > 0) {
            if (args.size() > maxArgs)
                return;
        }

        exec();
    }

    /**
     * Executes the command based on the user input. Only called if all checks passed.
     */
    public abstract void exec();
}
