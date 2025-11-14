package com.magicscreencinema.domain.exception;

public class InvalidDiscountException extends RuntimeException {
    public InvalidDiscountException(String message) {
        super(message);
    }
}
