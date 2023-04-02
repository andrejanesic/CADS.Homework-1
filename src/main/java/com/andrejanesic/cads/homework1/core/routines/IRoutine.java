package com.andrejanesic.cads.homework1.core.routines;

import com.andrejanesic.cads.homework1.core.exceptions.RoutineException;

/**
 * Common program routine. Wraps a user procedure.
 * <p>
 * For example: stopping the program, adding a new job, etc.
 */
public interface IRoutine {

    /**
     * Attempts to execute the routine with the given parameters. Parameters
     * should be supplied via the constructor.
     *
     * @throws RoutineException exception is thrown if the routine fails
     *                          during execution
     */
    void doRoutine() throws RoutineException;

    /**
     * Attempts to undo the routine with the given parameters. Parameters
     *      * should be supplied via the constructor.
     *
     * @throws RoutineException exception is thrown if the routine fails
     *                          during undoing
     */
    void undoRoutine() throws RoutineException;
}
