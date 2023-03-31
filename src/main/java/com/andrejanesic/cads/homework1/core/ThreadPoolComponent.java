package com.andrejanesic.cads.homework1.core;

import lombok.Getter;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Base component that runs on a separate thread and has its own thread pool.
 *
 * @param <V> Thread pool result type.
 */
public abstract class ThreadPoolComponent<V> extends ThreadedComponent {

    @Getter
    private final ExecutorService pool;

    @Getter
    private ExecutorCompletionService<V> results;

    /**
     * Base component with thread pool support.
     *
     * @param pool {@link ExecutorService} instance to use as thread pool.
     */
    public ThreadPoolComponent(ExecutorService pool) {
        this.pool = pool;
    }

    /**
     * Base component with thread pool support.
     */
    public ThreadPoolComponent() {
        this(Executors.newCachedThreadPool());
    }

    @Override
    public void main() {
        results = new ExecutorCompletionService<>(pool);
        keepAlive();
    }

    @Override
    public void beforeEnd() {
        pool.shutdown();
        super.beforeEnd();
    }
}
