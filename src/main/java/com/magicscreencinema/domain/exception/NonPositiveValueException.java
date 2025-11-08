package com.magicscreencinema.domain.exception;

public class NonPositiveValueException extends RuntimeException{
    public NonPositiveValueException(String message) {
        super(message);
    }

    public NonPositiveValueException() {
        super("Value can not be negative or 0");
    }
}
