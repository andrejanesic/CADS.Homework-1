package com.andrejanesic.cads.homework1.cli.input.commons;

import com.andrejanesic.cads.homework1.cli.input.commons.commands.CommandManager;
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


    @Inject
    public InputReader(CommandManager commandManager) {
    }

    @Override
    public void loop() throws ComponentException {
    }
}
