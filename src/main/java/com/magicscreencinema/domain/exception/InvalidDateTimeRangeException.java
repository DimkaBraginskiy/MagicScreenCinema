package com.magicscreencinema.domain.exception;

public class InvalidDateTimeRangeException extends RuntimeException {
    public InvalidDateTimeRangeException(String message) {
        super(message);
    }

    public InvalidDateTimeRangeException() {
        super("StartTime can not be bigger than EndTime");
    }
}
