package com.andrejanesic.cads.homework1.core.exceptions;

/**
 * Wrapper for checked exceptions.
 */
public class ComponentException extends Exception {

    public ComponentException() {
    }

    public ComponentException(String message) {
        super(message);
    }

    public ComponentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentException(Throwable cause) {
        super(cause);
    }

    public ComponentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
