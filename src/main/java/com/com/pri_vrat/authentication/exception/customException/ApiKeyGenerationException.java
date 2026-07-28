package com.com.pri_vrat.authentication.exception.customException;

public class ApiKeyGenerationException extends RuntimeException {
    public ApiKeyGenerationException() {
    }

    public ApiKeyGenerationException(String message) {
        super(message);
    }

    public ApiKeyGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiKeyGenerationException(Throwable cause) {
        super(cause);
    }

    public ApiKeyGenerationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
