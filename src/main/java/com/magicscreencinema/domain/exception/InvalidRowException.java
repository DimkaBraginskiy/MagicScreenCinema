package com.magicscreencinema.domain.exception;

public class InvalidRowException extends RuntimeException {
    public InvalidRowException(String message) {
        super(message);
    }

    public InvalidRowException() {
        super("Invalid row selected.");
    }
}