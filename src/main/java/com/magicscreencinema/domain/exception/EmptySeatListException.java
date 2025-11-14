package com.magicscreencinema.domain.exception;

public class EmptySeatListException extends RuntimeException {
    public EmptySeatListException(String message) {
        super(message);
    }
}
