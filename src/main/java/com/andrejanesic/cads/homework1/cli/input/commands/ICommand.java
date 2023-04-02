package com.andrejanesic.cads.homework1.cli.input.commands;

import com.andrejanesic.cads.homework1.core.exceptions.CLInputException;
import com.andrejanesic.cads.homework1.core.exceptions.UnexpectedRuntimeComponentException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.cli.CommandLine;

import java.util.List;

/**
 * Represents commands that the user can enter via the input module as a string.
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
    private boolean commandEnable = false;
    private String[] commands = null;
    /**
     * Whether the command has been recognized and parsed by this command.
     * Does not indicate successful execution, only recognition of the
     * command pattern in the user's input.
     */
    @Getter
    @Setter
    private boolean recognized = false;

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
     * Verifies the user input against this command's rules and runs
     * {@link #exec()} if command applicable.
     */
    public void parse() throws CLInputException {
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

        setRecognized(true);
        exec();
    }

    /**
     * Executes the command based on the user input. Only called if all
     * checks passed. This method should conduct any fine syntax checking
     * (such as checking for arguments, and so on.)
     */
    public abstract void exec() throws CLInputException;
}
