package com.andrejanesic.cads.homework1.core;

import com.andrejanesic.cads.homework1.utils.LoopRunnable;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * Base component with thread helper methods.
 */
public abstract class ThreadedComponent extends IComponent {

    private final List<ThreadLoopRunnable> registeredThreads = new LinkedList<>();

    @Override
    public void shutdown() {
        for (ThreadLoopRunnable threadLoopRunnable : registeredThreads) {
            try {
                threadLoopRunnable.loopRunnable.stop();
                threadLoopRunnable.thread.join();
            } catch (InterruptedException e) {
                // TODO handle
                throw new RuntimeException(e);
            }
        }
        super.shutdown();
    }

    /**
     * Starts a new single thread and automatically takes care of its shutdown (unless {@link #shutdown()} is
     * overridden.
     *
     * @param loopRunnable The {@link LoopRunnable} to execute on the thread.
     */
    public void startThread(LoopRunnable loopRunnable) {
        Thread thread = new Thread(loopRunnable);
        registeredThreads.add(new ThreadLoopRunnable(thread, loopRunnable));
        thread.start();
    }

    @AllArgsConstructor
    private static class ThreadLoopRunnable {
        Thread thread;
        LoopRunnable loopRunnable;
    }
}
