package org.example.persistence.exception;

public class CouldNotReadObjectException extends RuntimeException {
    public CouldNotReadObjectException(String message, Throwable cause) {
        super(message, cause);
    }

}
