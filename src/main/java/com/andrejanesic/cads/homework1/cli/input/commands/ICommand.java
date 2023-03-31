package com.andrejanesic.cads.homework1.cli.input.commands;

import com.andrejanesic.cads.homework1.core.exceptions.UnexpectedRuntimeComponentException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.cli.CommandLine;

import java.util.List;

/**
 * Used for parsing user args input.
 */
public abstract class ICommand {

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
    private String[] commands = null;

    public ICommand() {
    }

    /**
     * Word that activates this command.
     *
     * @param commands word(s) that activate this command
     * @return this
     * @throws UnexpectedRuntimeComponentException if less than one command
     *                                             string provided
     */
    ICommand command(String... commands) {
        if (commands.length < 1)
            throw new UnexpectedRuntimeComponentException(
                    "ICommand::commands commands argument must have at least " +
                            "1 element"
            );
        this.commandEnable = true;
        this.commands = commands;
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

            boolean activationCommand = false;
            for (String kw : commands) {
                if (args.get(0).equalsIgnoreCase(kw)) {
                    activationCommand = true;
                    break;
                }
            }
            if (!activationCommand) return;
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
