package com.andrejanesic.cads.homework1.cli.commons.commands;

import java.util.Arrays;
import java.util.List;

public class CommandManager {

    /**
     * Returns all available {@link ICommand} implementations.
     * @return Returns all available {@link ICommand} implementations.
     */
    public static List<ICommand> getCommands() {
        return Arrays.asList(new ICommand[] {
                new CommandAdd(),
        });
    }
}
