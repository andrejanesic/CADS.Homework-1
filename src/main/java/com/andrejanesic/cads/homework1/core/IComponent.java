package com.andrejanesic.cads.homework1.core;

/**
 * Base component.
 */
public abstract class IComponent {
    /**
     * Called at the start of the component lifecycle.
     */
    public void start() {
        afterStart();
    }

    /**
     * Called after starting the component, but before {@link #main()}.
     */
    public void afterStart() {

    }

    /**
     * The component's main task.
     */
    public void main() {

    }

    /**
     * Called for a graceful component shutdown, before {@link #end()}. Any clean-up should be done here.
     */
    public void beforeEnd() {

    }

    /**
     * Called at the end of the component lifecycle.
     */
    public void end() {
        beforeEnd();
    }
}
