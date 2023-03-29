package com.andrejanesic.cads.homework1.core.exceptions;

/**
 * Wrapper for runtime exceptions.
 */
public class RuntimeComponentException extends RuntimeException {

    public RuntimeComponentException() {
    }

    public RuntimeComponentException(String message) {
        super(message);
    }

    public RuntimeComponentException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeComponentException(Throwable cause) {
        super(cause);
    }

    public RuntimeComponentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
