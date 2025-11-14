package com.magicscreencinema.domain.exception;

public class RecursionException extends RuntimeException {
    public RecursionException(String message) {
        super(message);
    }
}
