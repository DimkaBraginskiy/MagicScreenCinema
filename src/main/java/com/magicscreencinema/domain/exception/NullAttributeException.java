package com.magicscreencinema.domain.exception;

public class NullAttributeException extends RuntimeException{
    public NullAttributeException(String message) {
        super(message);
    }

    public NullAttributeException() {
        super("Attribute can not be null");
    }
}
