package com.magicscreencinema.domain.exception;

public class DateInFutureException extends RuntimeException {
    public DateInFutureException(String message) {
        super(message);
    }
}