package com.magicscreencinema.persistence.exception;

public class CouldNotReadObjectException extends RuntimeException {
    public CouldNotReadObjectException(String message, Throwable cause) {
        super(message, cause);
    }

}
