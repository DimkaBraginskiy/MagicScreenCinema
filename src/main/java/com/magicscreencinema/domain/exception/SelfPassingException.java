package com.magicscreencinema.domain.exception;

public class SelfPassingException extends RuntimeException {
    public SelfPassingException(String message) {
        super(message);
    }

    public SelfPassingException() {
        super("Can not pass the same Object to itself");
    }
}
