package com.andrejanesic.cads.homework1.core.exceptions;

/**
 * These... these should never occur.
 */
public class UnexpectedRuntimeComponentException extends RuntimeComponentException {

    public UnexpectedRuntimeComponentException() {
    }

    public UnexpectedRuntimeComponentException(String message) {
        super(message);
    }

    public UnexpectedRuntimeComponentException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedRuntimeComponentException(Throwable cause) {
        super(cause);
    }

    public UnexpectedRuntimeComponentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
