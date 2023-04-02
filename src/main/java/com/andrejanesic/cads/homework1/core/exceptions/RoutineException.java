package com.andrejanesic.cads.homework1.core.exceptions;

public class RoutineException extends Exception {
    public RoutineException() {
    }

    public RoutineException(String message) {
        super(message);
    }

    public RoutineException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoutineException(Throwable cause) {
        super(cause);
    }

    public RoutineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
