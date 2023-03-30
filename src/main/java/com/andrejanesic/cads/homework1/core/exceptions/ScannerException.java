package com.andrejanesic.cads.homework1.core.exceptions;

public class ScannerException extends ComponentException {

    public ScannerException() {
    }

    public ScannerException(String message) {
        super(message);
    }

    public ScannerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScannerException(Throwable cause) {
        super(cause);
    }

    public ScannerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
