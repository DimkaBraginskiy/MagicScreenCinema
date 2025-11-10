package org.example.persistence.exception;

public class InvalidIdTypeException extends RuntimeException {
    public InvalidIdTypeException(String message) {
        super(message);
    }
}
