package com.magicscreencinema.domain.exception;

public class InvalidSeatNumberException extends RuntimeException {
    public InvalidSeatNumberException(String message) {
        super(message);
    }

    public InvalidSeatNumberException() {
        super("Seat number exceeds hall row width.");
    }
}
