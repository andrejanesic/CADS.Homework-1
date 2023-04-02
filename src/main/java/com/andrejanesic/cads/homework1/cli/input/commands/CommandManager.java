package com.andrejanesic.cads.homework1.cli.input.commands;

import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;
import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

@Singleton
@Getter
public class CommandManager implements ICommandManager {

    private final IDirectoryCrawler directoryCrawler;
    private final CommandAdd commandAdd;
    private final CommandClear commandClear;
    private final CommandGet commandGet;
    private final CommandQuery commandQuery;
    private final CommandStop commandStop;

    /**
     * Holds all commands
     *
     * @param commandAdd
     * @param commandClear
     * @param commandGet
     * @param commandQuery
     * @param commandStop
     */
    @Inject
    public CommandManager(
            IDirectoryCrawler directoryCrawler,
            CommandAdd commandAdd,
            CommandClear commandClear,
            CommandGet commandGet,
            CommandQuery commandQuery,
            CommandStop commandStop
    ) {
        this.directoryCrawler = directoryCrawler;
        this.commandAdd = commandAdd;
        this.commandClear = commandClear;
        this.commandGet = commandGet;
        this.commandQuery = commandQuery;
        this.commandStop = commandStop;
    }

    @Override
    public List<ICommand> getCommands() {
        return Arrays.asList(
                commandAdd,
                commandClear,
                commandGet,
                commandQuery,
                commandStop
        );
    }
}
