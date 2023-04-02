package com.andrejanesic.cads.homework1.core.routines;

import com.andrejanesic.cads.homework1.Main;
import com.andrejanesic.cads.homework1.core.exceptions.RoutineException;

/**
 * Routine for stopping/exiting the program gracefully.
 */
public class StopRoutine implements IRoutine {

    @Override
    public void doRoutine() throws RoutineException {
        synchronized (Main.getEndLock()) {
            Main.setEnd(true);
            Main.getEndLock().notifyAll();
        }
    }

    @Override
    public void undoRoutine() throws RoutineException {
        ;
    }
}
