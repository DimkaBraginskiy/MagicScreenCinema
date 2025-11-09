package org.example.persistence;

public class MissingIdException extends RuntimeException {
    public MissingIdException(String s) {
        super(s);
    }
}
