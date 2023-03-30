package com.andrejanesic.cads.homework1.core;

/**
 * Base component.
 */
public abstract class IComponent implements Runnable {

    private final Object keepAliveLock = new Object();
    private Thread componentThread;
    private boolean keepAlive = false;

    /**
     * Called at the start of the component lifecycle.
     */
    public void start() {
        componentThread = new Thread(this);
        componentThread.start();
        afterStart();
    }

    /**
     * Called after starting the component, but before {@link #main()}.
     */
    public void afterStart() {
        ;
    }

    /**
     * The component's main task.
     */
    public void main() {
        keepAlive();
    }

    /**
     * Convenience method to keep the thread running.
     */
    public void keepAlive() {
        synchronized (keepAliveLock) {
            keepAlive = true;
            while (keepAlive) {
                try {
                    keepAliveLock.wait();
                } catch (InterruptedException ignored) {
                    ;
                }
            }
        }
    }

    /**
     * Called for a graceful component shutdown, before {@link #end()}. Any clean-up should be done here.
     */
    public void beforeEnd() {
        ;
    }

    /**
     * Called at the end of the component lifecycle.
     */
    public void end() {
        keepAlive = false;
        keepAliveLock.notify();
        beforeEnd();
        try {
            componentThread.join();
        } catch (InterruptedException ignored) {
            ;
        }
    }

    @Override
    public void run() {
        main();
    }
}
