package com.magicscreencinema.domain.exception;

public class InvalidRowException extends RuntimeException {
    public InvalidRowException(String message) {
        super(message);
    }

    public InvalidRowException() {
        super("Seat row exceeds hall max rows.");
    }
}