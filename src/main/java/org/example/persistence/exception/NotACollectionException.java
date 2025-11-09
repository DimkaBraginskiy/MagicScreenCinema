package org.example.persistence.exception;

public class NotACollectionException extends RuntimeException {
    public NotACollectionException(String message) {
        super(message);
    }
}
