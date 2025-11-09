package org.example.persistence;

public class CouldNotPersistObjectException extends RuntimeException {
    public CouldNotPersistObjectException(String message) {
        super(message);
    }

    public CouldNotPersistObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
