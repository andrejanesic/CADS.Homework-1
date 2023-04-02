package com.andrejanesic.cads.homework1.core;

import lombok.Getter;
import lombok.Setter;

/**
 * Base component that runs on a separate thread.
 * <p>
 * Provides convenience methods for running the component on a separate
 * thread without having to implement thread support in subclasses.
 * <p>
 * The component's thread is managed directly by this class, through
 * afterStart() and beforeEnd() methods.
 *
 * @see IComponent
 */
public abstract class ThreadedComponent extends IComponent implements Runnable {

    /**
     * Lock used for synchronizing access to {@link #keepAlive}.
     */
    @Getter
    private final Object keepAliveLock = new Object();
    @Getter
    private Thread componentThread;
    /**
     * Whether the IComponent's main task should keep running or not.
     * Defaults to true.
     */
    @Getter
    @Setter
    private boolean keepAlive = true;

    @Override
    public void start() {
        if (isStarted()) return;
        componentThread = new Thread(this);
        componentThread.setName(getClass().getName());
        setStarted(true);
        setMain(true);
        // afterStart() called within run() here
        componentThread.start();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Should not be called directly, otherwise it will execute in the
     * current thread.
     * <p>
     * <b>Subclasses need to implement a mechanism of keeping the thread
     * alive with {@link #isKeepAlive()}/{@link #setKeepAlive(boolean)} and
     * {@link #keepAliveLock}.</b>
     */
    public abstract void main();

    /**
     * Keeps the thread running in a sleeping state. Convenience method.
     * Blocks the component's thread until awoken by ending.
     */
    public void keepAlive() {
        synchronized (keepAliveLock) {
            if (!keepAlive) return;
            while (keepAlive) {
                try {
                    keepAliveLock.wait();
                } catch (InterruptedException ignored) {
                    ;
                }
            }
        }
    }

    @Override
    public void end() {
        if (isEnded()) return;

        synchronized (keepAliveLock) {
            // beforeEnd(); will be called here, as part of run();
            keepAlive = false;
            keepAliveLock.notifyAll();
        }

        try {
            componentThread.join();
        } catch (InterruptedException ignored) {
            ;
        }
        setEnded(true);
    }

    /**
     * {@inheritDoc}
     *
     * <b>Note: this method is only called AFTER</b>
     */
    public abstract void beforeEnd();

    /**
     * <b>Should not be overridden by subclasses of ThreadedComponent.</b>
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void run() {
        afterStart();
        main();
        beforeEnd();
    }
}
