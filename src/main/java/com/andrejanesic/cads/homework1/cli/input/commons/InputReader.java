package com.andrejanesic.cads.homework1.cli.input.commons;

import com.andrejanesic.cads.homework1.cli.input.commands.CommandManager;
import com.andrejanesic.cads.homework1.core.exceptions.ComponentException;
import com.andrejanesic.cads.homework1.utils.LoopRunnable;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Listens for user input.
 */
@Singleton
public class InputReader extends LoopRunnable {


    @Inject
    public InputReader(CommandManager commandManager) {
    }

    @Override
    public void loop() throws ComponentException {
    }
}
