package com.magicscreencinema.domain.exception;

public class EmptyStringException extends RuntimeException{
    public EmptyStringException(String message) {
        super(message);
    }

    public EmptyStringException() {
        super("String cannot be empty");
    }
}
