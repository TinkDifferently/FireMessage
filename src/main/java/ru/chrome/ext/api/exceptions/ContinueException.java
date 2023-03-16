package ru.chrome.ext.api.exceptions;

public class ContinueException extends RuntimeException{
    public ContinueException() {
        super();
    }

    public ContinueException(String message) {
        super(message);
    }

    public ContinueException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContinueException(Throwable cause) {
        super(cause);
    }

    protected ContinueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
