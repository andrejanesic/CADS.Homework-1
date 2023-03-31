package com.andrejanesic.cads.homework1.core.routines;

/**
 * Singleton object used for activating and managing routines. Routines
 * should only be called via this class.
 */
public class RoutineManager {

    private RoutineManager() {

    }

    /**
     * Returns the singleton instance of RoutineManager.
     *
     * @return the singleton instance of RoutineManager
     */
    public RoutineManager getInstance() {
        return RoutineManagerInstance.INSTANCE;
    }

    private static class RoutineManagerInstance {
        private static final RoutineManager INSTANCE = new RoutineManager();
    }
}
