package com.magicscreencinema.persistence.exception;

public class ReferenceIntegrityException extends RuntimeException {
    public ReferenceIntegrityException(String message) {
        super("Reference integrety violation: " + message);
    }

    public ReferenceIntegrityException(String message, Throwable cause) {
        super(message, cause);
    }
}
