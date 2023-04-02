package com.andrejanesic.cads.homework1.core.exceptions;

public class JobQueueException extends ComponentException {

    public JobQueueException() {
    }

    public JobQueueException(String message) {
        super(message);
    }

    public JobQueueException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobQueueException(Throwable cause) {
        super(cause);
    }

    public JobQueueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
