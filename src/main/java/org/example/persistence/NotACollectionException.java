package org.example.persistence;

public class NotACollectionException extends RuntimeException {
    public NotACollectionException(String message) {
        super(message);
    }
}
