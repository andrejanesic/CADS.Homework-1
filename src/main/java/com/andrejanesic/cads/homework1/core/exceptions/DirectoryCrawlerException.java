package com.andrejanesic.cads.homework1.core.exceptions;

public class DirectoryCrawlerException extends ComponentException {

    public DirectoryCrawlerException() {
    }

    public DirectoryCrawlerException(String message) {
        super(message);
    }

    public DirectoryCrawlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DirectoryCrawlerException(Throwable cause) {
        super(cause);
    }

    public DirectoryCrawlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
