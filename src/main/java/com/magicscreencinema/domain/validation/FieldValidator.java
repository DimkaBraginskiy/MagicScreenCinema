package com.magicscreencinema.domain.validation;

import com.magicscreencinema.domain.exception.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class FieldValidator {
    /**
     * validates that a String value is neither null nor empty
     */
    public static String validateNullOrEmptyString(String value, String fieldName){
        if (value == null) {
            throw new NullAttributeException(fieldName + "  can not be null");
        }
        if (value.trim().isEmpty()) {
            throw new EmptyStringException(fieldName + " can not be empty");
        }
        return value;
    }

    /**
     * validates that a String value is neither null nor empty
     */
    public static String validateNullableString(String value, String fieldName){
        if (value != null && value.isEmpty()) {
            throw new EmptyStringException(fieldName + " can not be empty");
        }
        return value;
    }

    /**
     * validates that long value is positive
     */
    public static long validatePositiveLong(long value, String fieldName) {
        if (value <= 0) {
            throw new NonPositiveValueException(fieldName + " must be a positive value ( > 0).");
        }
        return value;
    }

    /**
     * validates that int value is positive
     */
    public static int validatePositiveInt(int value, String fieldName) {
        if (value <= 0) {
            throw new NonPositiveValueException(fieldName + " must be a positive value ( > 0).");
        }
        return value;
    }

    /**
     * validates a date that must NOT be in the future
     */
    public static LocalDate validateDateNotInTheFuture(LocalDate value, String fieldName) {
        if (value == null) {
            throw new NullAttributeException(fieldName + " can not be null");
        }

        if (value.isAfter(LocalDate.now())) {
            throw new DateInFutureException(fieldName + " can not be in the future");
        }
        return value;
    }

    /**
     * validates a date that must NOT be in the past
     */
    public static LocalDate validateDateNotInThePast(LocalDate value, String fieldName) {
        if (value == null) {
            throw new NullAttributeException(fieldName + " can not be null");
        }

        if (value.isBefore(LocalDate.now())) {
            throw new DateInPastException(fieldName + " can not be in the past");
        }
        return value;
    }

    /**
     * validates a date-time that must NOT be in the future
     */
    public static LocalDateTime validateDateTimeNotInTheFuture(LocalDateTime value, String fieldName) {
        if (value == null) {
            throw new NullAttributeException(fieldName + " can not be null");
        }

        if (value.isAfter(LocalDateTime.now())) {
            throw new DateInFutureException(fieldName + " can not be in the future");
        }
        return value;
    }

    /**
     * validates a date-time that must NOT be in the past
     */
    public static LocalDateTime validateDateTimeNotInThePast(LocalDateTime value, String fieldName) {
        if (value == null) {
            throw new NullAttributeException(fieldName + " can not be null");
        }

        if (value.isBefore(LocalDateTime.now())) {
            throw new DateInPastException(fieldName + " can not be in the past");
        }
        return value;
    }

    /**
     * validates that an object is NOT null (e.g. Enum)
     */
    public static <T> T validateObjectNotNull(T value, String fieldName) {
        if (value == null) {
            throw new NullAttributeException(fieldName + " can not be null");
        }
        return value;
    }
}
