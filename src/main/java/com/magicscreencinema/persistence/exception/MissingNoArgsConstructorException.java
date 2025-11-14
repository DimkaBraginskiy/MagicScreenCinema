package com.magicscreencinema.persistence.exception;

public class MissingNoArgsConstructorException extends RuntimeException {
    public MissingNoArgsConstructorException(String message) {
        super(message);
    }
}
