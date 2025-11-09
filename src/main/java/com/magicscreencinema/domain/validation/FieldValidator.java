package com.magicscreencinema.domain.validation;

import com.magicscreencinema.domain.exception.*;
import com.magicscreencinema.domain.model.Seat;
import com.magicscreencinema.domain.model.Hall;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class FieldValidator {
    /**
     * validates that a String value is neither null nor empty
     */
    public static String validateNullOrEmptyString(String value, String fieldName){
        if (value == null) {
            throw new NullAttributeException(fieldName + " can not be null");
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
            if (intValue <= 0) {
                throw new NonPositiveValueException(fieldName + " must be a positive value ( > 0).");
            }
            return value;
        } else if(value instanceof Long longValue){
            if (longValue <= 0) {
                throw new NonPositiveValueException(fieldName + " must be a positive value ( > 0).");
            }
            return value;
        } else if(value instanceof Double doubleValue){
            if (doubleValue <= 0) {
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
            throw new InvalidDateTimeRangeException("StartTime can not be bigger than EndTime");
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
    public static <T> T validateObjectRecursion(T passed, T passedTo){
        if(passedTo.equals(passed)){
            throw new RecursionException("An object cannot have a recursive association with itself");
        }
        return passed;
    }

    /**
     *  validates a List of DayOfWeekEnums
     *  Identifies duplicates by setting List's values to a Set.
     *  Eliminates possibility of null values in a List.
     */
    public static List<DayOfWeek> validateDayOfWeekList(List<DayOfWeek> dayOfWeek, String fieldName) {
        validateObjectNotNull(dayOfWeek, fieldName);

        Set<DayOfWeek> uniqueDays = new HashSet<>();
        for (DayOfWeek day : dayOfWeek) {
            validateObjectNotNull(day, fieldName);
            if (!uniqueDays.add(day)) {
                throw new DuplicateDayException("Duplicate day '" + day + "' in " + fieldName + " is not allowed");
            }
        }

        return List.copyOf(dayOfWeek);
    }

    /**
     * validates that all seats are not null
     * validates seatRows > maxRow -> throw InvalidRowException
     * validates seatNumber > rowWidth -> throw InvalidSeatNumberException
     * connects every seat to passed as an argument hall
     */
    public static List<Seat> validateSeatsInHallNotNull(List<Seat> seats, Hall hall) {
        validateObjectNotNull(seats, "Seats list");
        validateObjectNotNull(hall, "Hall");

        int maxRow = hall.getMaxRow();
        int rowWidth = hall.getRowWidth();

        for (Seat seat : seats) {
            validateObjectNotNull(seat, "Seat");

            if (seat.getRow() > maxRow) {
                throw new InvalidRowException("Seat row exceeds hall max rows.");
            }

            if (seat.getSeatNumber() > rowWidth*seat.getRow()) {
                throw new InvalidSeatNumberException("Seat number exceeds hall row width.");
            }

            seat.setHall(hall);
        }

        return seats;
    }

    /**
     * validates a seat List for now being null and empty
     */
    public static List<Seat> validateSeatList(List<Seat> seats, String fieldName){
        validateObjectNotNull(seats, fieldName);

        if(seats.isEmpty()){
            throw new InvalidDateTimeRangeException(fieldName + " cannot be empty");
        }

        return List.copyOf(seats);
    }
}
