package com.andrejanesic.cads.homework1.core.routines;

import com.andrejanesic.cads.homework1.core.exceptions.RoutineException;

import java.util.Stack;

/**
 * Singleton object used for activating and managing routines. Routines
 * should only be called via this class.
 */
public class RoutineManager {

    private final Stack<IRoutine> undoable = new Stack<>();
    private final Stack<IRoutine> redoable = new Stack<>();

    private RoutineManager() {

    }

    /**
     * Returns the singleton instance of RoutineManager.
     *
     * @return the singleton instance of RoutineManager
     */
    public static RoutineManager getInstance() {
        return RoutineManagerInstance.INSTANCE;
    }

    /**
     * Adds a routine to the stack of executable routines, and attempts to
     * execute it by calling {@link #doRoutine()}.
     *
     * @param routine routine to execute
     * @throws RoutineException the exception thrown by the routine, if any
     */
    public void addRoutine(IRoutine routine) throws RoutineException {
        redoable.push(routine);
        doRoutine();
    }

    /**
     * Attempts to execute the last added (re-)doable routine. If an
     * exception is thrown, the routine is not added to undoable actions.
     *
     * @throws RoutineException the exception thrown by the routine, if any
     */
    public void doRoutine() throws RoutineException {
        if (redoable.empty()) return;
        IRoutine routine = redoable.pop();
        routine.doRoutine();
        undoable.push(routine);
    }

    /**
     * Attempts to execute the last added undoable routine. If an exception
     * is thrown, the routine is not added to undoable actions.
     *
     * @throws RoutineException the exception thrown by the routine, if any
     */
    public void undoRoutine() throws RoutineException {
        if (undoable.empty()) return;
        IRoutine routine = undoable.pop();
        routine.undoRoutine();
        redoable.push(routine);
    }

    private static class RoutineManagerInstance {
        private static final RoutineManager INSTANCE = new RoutineManager();
    }
}
