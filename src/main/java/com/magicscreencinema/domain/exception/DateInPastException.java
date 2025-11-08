package com.magicscreencinema.domain.exception;

public class DateInPastException extends RuntimeException {
    public DateInPastException(String message) {
        super(message);
    }

    public DateInPastException() {
        super("The requested reservation time is in the past.");
    }
}