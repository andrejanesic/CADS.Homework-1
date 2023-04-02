package com.andrejanesic.cads.homework1.core.exceptions;

public class CLInputException extends Exception {

    public CLInputException() {
    }

    public CLInputException(String message) {
        super(message);
    }

    public CLInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public CLInputException(Throwable cause) {
        super(cause);
    }

    public CLInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
