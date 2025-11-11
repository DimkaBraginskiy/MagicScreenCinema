package com.magicscreencinema.persistence.exception;

public class MissingIdException extends RuntimeException {
    public MissingIdException(String s) {
        super(s);
    }
}
