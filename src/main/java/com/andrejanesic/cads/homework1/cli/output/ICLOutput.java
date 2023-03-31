package com.andrejanesic.cads.homework1.cli.output;

import com.andrejanesic.cads.homework1.core.IComponent;

/**
 * Command-line output component.
 */
public abstract class ICLOutput extends IComponent {

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
