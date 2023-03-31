package com.andrejanesic.cads.homework1.core.routines;

import com.andrejanesic.cads.homework1.Main;

/**
 * Routine for stopping/exiting the program gracefully.
 */
public class StopRoutine implements IRoutine {

    @Override
    public void execute() {
        Main.end();
    }
}
