package com.magicscreencinema.domain.exception;

public class DateInFutureException extends RuntimeException {
    public DateInFutureException(String message) {
        super(message);
    }

    public DateInFutureException() {
        super("The provided date of birth is in the future.");
    }
}