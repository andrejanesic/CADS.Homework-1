package com.andrejanesic.cads.homework1.core;

/**
 * Base component that runs on a separate thread.
 */
public abstract class ThreadedComponent extends IComponent implements Runnable {

    private final Object keepAliveLock = new Object();
    private Thread componentThread;
    private boolean keepAlive = false;

    @Override
    public void start() {
        componentThread = new Thread(this);
        componentThread.start();
        afterStart();
    }

    @Override
    public void afterStart() {
        ;
    }

    @Override
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

    @Override
    public void beforeEnd() {
        ;
    }

    @Override
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
