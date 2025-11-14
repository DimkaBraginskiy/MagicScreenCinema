package com.magicscreencinema.domain.exception;

public class InvalidPhoneNumberFormatException extends RuntimeException {
    public InvalidPhoneNumberFormatException(String message) {
        super(message);
    }
}
