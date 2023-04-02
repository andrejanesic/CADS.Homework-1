package com.andrejanesic.cads.homework1.core.routines;

import com.andrejanesic.cads.homework1.Main;
import com.andrejanesic.cads.homework1.core.exceptions.RoutineException;

/**
 * Routine for stopping/exiting the program gracefully.
 */
public class StopRoutine implements IRoutine {

    @Override
    public void doRoutine() throws RoutineException {
        Main.end();
    }

    @Override
    public void undoRoutine() throws RoutineException {
        ;
    }
}
