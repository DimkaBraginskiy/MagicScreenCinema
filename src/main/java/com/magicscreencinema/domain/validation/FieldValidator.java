package com.magicscreencinema.domain.validation;

import com.magicscreencinema.domain.enums.DayOfWeekEnum;
import com.magicscreencinema.domain.exception.*;
import com.magicscreencinema.domain.model.Seat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
     * validates that Generic numerical value is positive
     */
    public static<T> T validatePositiveNumber(T value, String fieldName){
        if(value instanceof Integer intValue){
            if (intValue < 0) {
                throw new NonPositiveValueException(fieldName + " must be a positive value ( > 0).");
            }
            return value;
        } else if(value instanceof Long longValue){
            if (longValue < 0) {
                throw new NonPositiveValueException(fieldName + " must be a positive value ( > 0).");
            }
            return value;
        } else if(value instanceof Double doubleValue){
            if (doubleValue < 0) {
                throw new NonPositiveValueException(fieldName + " must be a positive value ( > 0).");
            }
            return value;
        } else {
            throw new IllegalArgumentException("Unsupported number type for validation");

        }
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

    public static void validateStartTimeIsAfterEndTime(LocalDateTime startTime, LocalDateTime endTime){
        if(startTime.isAfter(endTime)){
            throw new InvalidDateTimeRangeException();
        }
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

    /**
     * validates that an object provided is different from one to which we pass
     * ALLOWS NULL value
     */
    public static <T> T validateObjectSelfPassing(T passed, T passedTo, String passedName, String passedToName){
        if(passedTo == passed){
            throw new SelfPassingException("Can not pass " + passedName + " to " + passedToName);
        }
        return passed;
    }


    public static List<DayOfWeekEnum> validateDayOfWeekList(List<DayOfWeekEnum> dayOfWeek, String fieldName) {
        validateObjectNotNull(dayOfWeek, fieldName);

        if (dayOfWeek.size() > 7) {
            throw new IllegalArgumentException(fieldName + " Cannot have more than 7 days in a week");
        }

        Set<DayOfWeekEnum> uniqueDays = dayOfWeek.stream()
                .peek(day -> FieldValidator.validateObjectNotNull(day, fieldName))
                .collect(Collectors.toSet());

        if (uniqueDays.size() != dayOfWeek.size()) {
            throw new IllegalArgumentException("Duplicate days in " + fieldName + " are not allowed");
        }

        return List.copyOf(dayOfWeek);
    }

    public static List<Seat> validateSeatList(List<Seat> seats, String fieldName){
        validateObjectNotNull(seats, fieldName);

        if(seats.isEmpty()){
            throw new InvalidDateTimeRangeException(fieldName + " cannot be empty");
        }

        return List.copyOf(seats);
    }
}
