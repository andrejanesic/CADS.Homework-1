package com.andrejanesic.cads.homework1.core;

import lombok.Getter;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Base component that runs on a separate thread and has its own thread pool.
 *
 * <p>Unlike {@link MultiThreadedComponent}, ThreadPoolThreadedComponent uses
 * {@link ExecutorService} as a thread pool for managing threads.</p>
 *
 * <p>The lifecycle of threads (starting and clean-up) is managed by this
 * class. Subclasses should only use the thread pool for submitting new jobs,
 * etc.</p>
 *
 * @param <V> Thread pool result type.
 */
public abstract class ThreadPoolThreadedComponent<V> extends ThreadedComponent {

    @Getter
    private final ExecutorService pool;

    @Getter
    private ExecutorCompletionService<V> results;

    /**
     * Constructor with configurable {@link ExecutorService}.
     *
     * @param pool ExecutorService instance to use as thread pool
     */
    public ThreadPoolThreadedComponent(ExecutorService pool) {
        this.pool = pool;
    }

    /**
     * Default constructor.
     */
    public ThreadPoolThreadedComponent() {
        this(Executors.newCachedThreadPool());
    }

    @Override
    public void afterStart() {
        results = new ExecutorCompletionService<>(pool);
    }

    @Override
    public void beforeEnd() {
        pool.shutdown();
    }
}
