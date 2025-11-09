package com.magicscreencinema.domain.exception;

public class DateInPastException extends RuntimeException {
    public DateInPastException(String message) {
        super(message);
    }
}