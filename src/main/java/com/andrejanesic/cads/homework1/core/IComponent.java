package com.andrejanesic.cads.homework1.core;

import lombok.Getter;
import lombok.Setter;

/**
 * Base component&mdash;a class that provides important functionality to the
 * application.
 * <p>
 * Components may depend on each other. Beware of this when calling methods in
 * other components as the components, or some of their methods, may not be
 * available.
 * <p>
 * Components do not start themselves, rather, they are used by ICore or any
 * class in charge of controlling the "life cycle" of the application.
 * <p>
 * A component's life cycle consists of three phases: start, main and end.
 * The start phase consists of {@link #start()} and {@link #afterStart()}
 * methods. The main phase consists of {@link #main()}, and should be
 * overridden by subclasses to implement functionality. The end phase
 * consists of {@link #beforeEnd()} and {@link #end()} methods.
 * <p>
 * Each life cycle phase is accompanied by changes in the {@link #started},
 * {@link #main} and {@link #ended} booleans, indicating the component's
 * state. A component's functionality is considered to be "available" if it
 * is currently in the main stage.
 *
 * @see ICore
 */
public abstract class IComponent {

    /**
     * Indicates whether the component is past its start life cycle period.
     * <p>
     * A component that is started successfully can enter its main phase.
     */
    @Setter
    @Getter
    private boolean started = false;

    /**
     * Indicates whether the component is in its main life cycle phase. If
     * true, the component's functionality is considered available to other
     * components.
     */
    @Setter
    @Getter
    private boolean main = false;

    /**
     * Indicates whether the component is past its end life cycle period.
     * <p>
     * A component that is ended can no longer be used.
     */
    @Setter
    @Getter
    private boolean ended = false;

    /**
     * Called at the start of the component lifecycle. Initiates the
     * component variables. <b>Should not be overridden by implementing
     * classes.</b>
     * <p>
     * Implementing classes should override {@link #afterStart()}} method for
     * any start-up work (or use the constructor).
     * <p>
     * Subsequent method calls include {@link #main()}.
     * <p>
     * As a result, {@link #started} is set to true.
     * <p>
     * This method must call {@link #afterStart()} after completing the
     * initialization of any functionality of this interface that may be
     * critical to how implementing subclasses work.
     */
    public void start() {
        if (started) return;
        afterStart();
        started = true;
        main = true;
        main();
    }

    /**
     * Called after starting the component, but before {@link #main()}. May
     * be overridden by subclasses. Overriding subclasses <b>must call
     * afterStart() on superclass at the beginning of the procedure.</b>
     * <p>
     * Completes the initialization of the class. This may include
     * instantiating variables, calling methods, etc.
     */
    public abstract void afterStart();

    /**
     * The component's main task; called by {@link #start()}.
     */
    public abstract void main();

    /**
     * Called for a graceful component shutdown, before {@link #end()}. Any
     * clean-up should be done here. <b>Overriding subclasses must call
     * beforeEnd() on superclass at the end of the procedure.</b>
     */
    public abstract void beforeEnd();

    /**
     * Called at the end of the component lifecycle. Completes the
     * component's life cycle.
     * <p>
     * As a result, {@link #ended} is set to true.
     * <p>
     * This method must call {@link #beforeEnd()} before conducting any
     * critical operations (operations that may affect the functionality of
     * the class).
     */
    public void end() {
        if (ended) return;
        beforeEnd();
        ended = true;
    }
}
