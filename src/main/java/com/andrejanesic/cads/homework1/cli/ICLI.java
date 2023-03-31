package com.andrejanesic.cads.homework1.cli;

import com.andrejanesic.cads.homework1.core.MultiThreadedComponent;

/**
 * Command-line interface component.
 */
public abstract class ICLI extends MultiThreadedComponent {

    /**
     * Sends an info message to the user.
     *
     * @param message Message string.
     */
    public abstract void info(String message);

    /**
     * Logs a warning message to the user.
     *
     * @param message Message string.
     */
    public abstract void warning(String message);

    /**
     * Sends an error message to the user.
     *
     * @param message Message string.
     */
    public abstract void error(String message);
}
