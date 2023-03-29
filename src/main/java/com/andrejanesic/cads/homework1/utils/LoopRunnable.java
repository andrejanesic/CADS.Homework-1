package com.andrejanesic.cads.homework1.utils;

import com.andrejanesic.cads.homework1.core.exceptions.DirectoryCrawlerException;
import lombok.Getter;

/**
 * Runnable class with a recurring task.
 */
public abstract class LoopRunnable implements Runnable {

    @Getter
    private boolean alive;

    @Override
    public void run() throws RuntimeException {
        while (alive) {
            loop();
        }
    }

    /**
     * Process that should be run in a while-true loop until {@link #stop()} is called.
     */
    public abstract void loop() throws RuntimeException;

    /**
     * Gracefully stops the thread.
     */
    public void stop() {
        alive = false;
    }
}
