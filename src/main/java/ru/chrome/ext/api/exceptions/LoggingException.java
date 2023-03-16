package ru.chrome.ext.api.exceptions;

import ru.chrome.ext.api.logger.IUseFileLogger;

public class LoggingException extends RuntimeException implements IUseFileLogger {
    public LoggingException() {
        super();
        log("Exception");
    }

    public LoggingException(String message) {
        super(message);
        log("Exception: "+message);
    }

    public LoggingException(String message, Throwable cause) {
        super(message, cause);
        log("Exception: "+message+cause);
    }

    public LoggingException(Throwable cause) {
        super(cause);
    }

    protected LoggingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
