package com.andrejanesic.cads.homework1.core;

/**
 * Base component class.
 */
public abstract class IComponent {

    /**
     * Initializes the component or starts its main task. May or may not launch multiple threads as a result, depending
     * on the component.
     */
    public void init() {
        ;
    }

    /**
     * Called for a graceful component shutdown.
     */
    public void shutdown() {
        ;
    }
}
