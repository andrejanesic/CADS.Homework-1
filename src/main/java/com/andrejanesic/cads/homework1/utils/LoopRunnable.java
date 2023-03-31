package com.andrejanesic.cads.homework1.utils;

import com.andrejanesic.cads.homework1.core.exceptions.ComponentException;
import com.andrejanesic.cads.homework1.core.exceptions.UnexpectedRuntimeComponentException;
import lombok.Getter;

/**
 * Runnable class with a recurring task.
 */
public abstract class LoopRunnable extends StopRunnable {

    @Getter
    private boolean alive = true;

    @Override
    public void run() {
        while (alive) {
            try {
                loop();
            } catch (ComponentException e) {
                throw new UnexpectedRuntimeComponentException(e);
            }
        }
    }

    /**
     * Process that should be run in a while-true loop until {@link #stop()} is called.
     */
    public abstract void loop() throws ComponentException;

    @Override
    public void stop() {
        alive = false;
    }
}
