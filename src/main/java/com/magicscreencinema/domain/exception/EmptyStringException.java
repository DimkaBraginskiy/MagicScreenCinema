package com.magicscreencinema.domain.exception;

public class EmptyStringException extends RuntimeException{
    public EmptyStringException(String message) {
        super(message);
    }
}
