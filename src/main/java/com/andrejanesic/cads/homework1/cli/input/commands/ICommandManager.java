package com.andrejanesic.cads.homework1.cli.input.commands;

import com.andrejanesic.cads.homework1.directoryCrawler.IDirectoryCrawler;

import java.util.List;

/**
 * Wrapper for managing commands. Commands shouldn't be instantiated
 * directly, but taken via this interface.
 */
public interface ICommandManager {

    /**
     * Returns all available {@link ICommand} implementations.
     *
     * @return Returns all available {@link ICommand} implementations.
     */
    List<ICommand> getCommands();

    IDirectoryCrawler getDirectoryCrawler();

    CommandAdd getCommandAdd();

    CommandClear getCommandClear();

    CommandQuery getCommandQuery();

    CommandStop getCommandStop();

}
