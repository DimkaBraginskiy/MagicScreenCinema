package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.DateInPastException;
import com.magicscreencinema.domain.exception.InvalidDateTimeRangeException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ShiftTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreateShift() {
        LocalDateTime start = LocalDateTime.of(2025, 12, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2025, 12, 1, 16, 0);
        Shift shift = new Shift(start, end);

        assertEquals(start, shift.getStartTime());
        assertEquals(end, shift.getEndTime());
    }

    @Test
    public void constructor_WithPastStartTimeField_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2023, 12, 1, 12, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 1, 16, 0);
            new Shift(start, end);
        });
        assertEquals("Start Time can not be in the past", exception.getMessage());
    }

    @Test
    public void constructor_WithPastEndTimeField_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2025, 12, 1, 12, 0);
            LocalDateTime end = LocalDateTime.of(2023, 12, 1, 16, 0);
            new Shift(start, end);
        });
        assertEquals("End Time can not be in the past", exception.getMessage());
    }

    @Test
    public void constructor_WithNullStartTimeField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            LocalDateTime end = LocalDateTime.of(2025, 12, 1, 16, 0);
            new Shift(null, end);
        });
        assertEquals("Start Time can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithNullEndTimeField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2025, 12, 1, 12, 0);
            new Shift(start, null);
        });
        assertEquals("End Time can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithInvalidDateTimeRange_ShouldThrowInvalidDateTimeRangeException() {
        InvalidDateTimeRangeException exception = assertThrows(InvalidDateTimeRangeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2025, 12, 1, 16, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 1, 12, 0);
            new Shift(start, end);
        });
        assertEquals("StartTime can not be bigger than EndTime", exception.getMessage());
    }

    @Test
    public void setStartTime_WithPastStartTimeParameter_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2025, 12, 1, 12, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 1, 16, 0);
            Shift shift = new Shift(start, end);
            shift.setStartTime(start.minusYears(1));
        });
        assertEquals("Start Time can not be in the past", exception.getMessage());
    }

    @Test
    public void setEndTime_WithPastEndTimeParameter_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2025, 12, 1, 12, 0);
            LocalDateTime end = LocalDateTime.of(2023, 12, 1, 16, 0);
            Shift shift = new Shift(start, end);
            shift.setEndTime(end.minusYears(1));
        });
        assertEquals("End Time can not be in the past", exception.getMessage());
    }

    @Test
    public void setStartTime_WithNullStartTimeParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2025, 12, 1, 12, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 1, 16, 0);
            Shift shift = new Shift(start, end);
            shift.setStartTime(null);
        });
        assertEquals("Start Time can not be null", exception.getMessage());
    }

    @Test
    public void setEndTime_WithNullEndTimeParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2025, 12, 1, 12, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 1, 16, 0);
            Shift shift = new Shift(start, end);
            shift.setEndTime(null);
        });
        assertEquals("End Time can not be null", exception.getMessage());
    }

    @Test
    public void setStartTime_WithStartTimeAfterEndTime_ShouldThrowInvalidDateTimeRangeException() {
        InvalidDateTimeRangeException exception = assertThrows(InvalidDateTimeRangeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2025, 12, 1, 12, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 1, 16, 0);
            Shift shift = new Shift(start, end);
            shift.setStartTime(start.plusHours(6));
        });
        assertEquals("StartTime can not be bigger than EndTime", exception.getMessage());
    }

    @Test
    public void setEndTime_WithStartTimeAfterEndTime_ShouldThrowInvalidDateTimeRangeException() {
        InvalidDateTimeRangeException exception = assertThrows(InvalidDateTimeRangeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2025, 12, 1, 12, 0);
            LocalDateTime end = LocalDateTime.of(2025, 12, 1, 16, 0);
            Shift shift = new Shift(start, end);
            shift.setEndTime(start.minusHours(6));
        });
        assertEquals("StartTime can not be bigger than EndTime", exception.getMessage());
    }

    @Test
    public void setStartTime_WithValidStartTimeParameter_ShouldChangeStartTime() {
        LocalDateTime start = LocalDateTime.of(2025, 12, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2025, 12, 1, 16, 0);
        Shift shift = new Shift(start, end);
        shift.setStartTime(start.plusHours(1));

        assertEquals(start.plusHours(1), shift.getStartTime());
    }

    @Test
    public void setEndTime_WithValidEndTimeParameter_ShouldChangeEndTime() {
        LocalDateTime start = LocalDateTime.of(2025, 12, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2025, 12, 1, 16, 0);
        Shift shift = new Shift(start, end);
        shift.setEndTime(end.plusHours(1));

        assertEquals(end.plusHours(1), shift.getEndTime());
    }
}
