package com.magicscreencinema.domain.exception;

public class RecursionException extends RuntimeException {
    public RecursionException(String message) {
        super(message);
    }

    public RecursionException() {
        super("Can not pass the same Object to itself");
    }
}
