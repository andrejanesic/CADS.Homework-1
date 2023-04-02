package com.andrejanesic.cads.homework1.exceptionHandler;

import com.andrejanesic.cads.homework1.core.IComponent;

/**
 * Handles exceptions. May be used anywhere in the software for easy
 * exception handling.
 */
public abstract class IExceptionHandler extends IComponent {

    /**
     * Accepts an {@link Exception} and handles it accordingly, without
     * producing new exceptions.
     *
     * @param e exception to handle
     */
    public abstract void handle(Exception e);
}
