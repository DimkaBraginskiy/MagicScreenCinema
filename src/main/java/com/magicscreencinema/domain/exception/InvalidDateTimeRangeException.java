package com.magicscreencinema.domain.exception;

public class InvalidDateTimeRangeException extends RuntimeException {
    public InvalidDateTimeRangeException(String message) {
        super(message);
    }
}
