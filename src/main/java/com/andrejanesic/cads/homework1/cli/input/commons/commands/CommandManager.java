package com.andrejanesic.cads.homework1.cli.input.commons.commands;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

/**
 * Holds all commands.
 */
@Singleton
public class CommandManager {

    private final CommandAdd commandAdd;
    private final CommandClear commandClear;
    private final CommandGet commandGet;
    private final CommandQuery commandQuery;
    private final CommandStop commandStop;
    private final CommandError commandError;

    /**
     * Holds all commands
     *
     * @param commandAdd
     * @param commandClear
     * @param commandGet
     * @param commandQuery
     * @param commandStop
     * @param commandError
     */
    @Inject
    public CommandManager(
            CommandAdd commandAdd,
            CommandClear commandClear,
            CommandGet commandGet,
            CommandQuery commandQuery,
            CommandStop commandStop,
            CommandError commandError
    ) {
        this.commandAdd = commandAdd;
        this.commandClear = commandClear;
        this.commandGet = commandGet;
        this.commandQuery = commandQuery;
        this.commandStop = commandStop;
        this.commandError = commandError;
    }

    /**
     * Returns all available {@link ICommand} implementations.
     *
     * @return Returns all available {@link ICommand} implementations.
     */
    public List<ICommand> getCommands() {
        return Arrays.asList(
                commandAdd,
                commandClear,
                commandGet,
                commandQuery,
                commandStop,
                commandError
        );
    }
}
