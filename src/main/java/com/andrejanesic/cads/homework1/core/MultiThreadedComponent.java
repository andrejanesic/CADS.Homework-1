package com.andrejanesic.cads.homework1.core;

import com.andrejanesic.cads.homework1.utils.StopRunnable;
import lombok.AllArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * Base component that runs on a separate thread, and may start more threads.
 *
 * <p>All threads started by this component are cleaned up by this class.
 * Subclasses should not manage threads directly.</p>
 *
 * @see ThreadedComponent
 */
public abstract class MultiThreadedComponent extends ThreadedComponent {

    private final List<ThreadStopRunnable> registeredThreads = new LinkedList<>();

    @Override
    public void beforeEnd() {
        for (ThreadStopRunnable threadStopRunnable : registeredThreads) {
            try {
                threadStopRunnable.stopRunnable.stop();
                if (!threadStopRunnable.thread.isDaemon())
                    threadStopRunnable.thread.join();
            } catch (InterruptedException e) {
                // TODO handle
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Starts a new single thread with the given task; clean-up of the thread
     * is taken care of by MultiThreadedComponent.
     *
     * @param stopRunnable the {@link StopRunnable} to execute on the new thread
     * @see MultiThreadedComponent
     */
    public void startNewThread(StopRunnable stopRunnable) {
        startNewThread(stopRunnable, false);
    }

    /**
     * Starts a new single thread with the given task; clean-up of the thread
     * is taken care of by MultiThreadedComponent.
     *
     * @param stopRunnable the {@link StopRunnable} to execute on the new thread
     * @param daemon       whether the thread should be marked a daemon
     * @see MultiThreadedComponent
     */
    public void startNewThread(StopRunnable stopRunnable, boolean daemon) {
        Thread thread = new Thread(stopRunnable);
        thread.setDaemon(daemon);
        thread.setName(stopRunnable.getClass().getName());
        registeredThreads.add(new ThreadStopRunnable(thread, stopRunnable));
        thread.start();
    }

    @AllArgsConstructor
    private static class ThreadStopRunnable {
        Thread thread;
        StopRunnable stopRunnable;
    }
}
