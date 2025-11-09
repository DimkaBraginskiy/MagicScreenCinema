package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.*;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class LimitedDiscountTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreateLimitedDiscount() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
        LimitedDiscount discount = new LimitedDiscount(start, end);

        assertNotNull(discount);
        assertEquals(start, discount.getStartTime());
        assertEquals(end, discount.getEndTime());
    }

    @Test
    public void constructor_WithStartTimeInPastField_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2025, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            new LimitedDiscount(start, end);
        });
        assertEquals("Start Time can not be in the past", exception.getMessage());
    }

    @Test
    public void constructor_WithEndTimeInPastField_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2025, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2025, 3, 10, 12, 0);
            new LimitedDiscount(start, end);
        });
        assertEquals("End Time can not be in the past", exception.getMessage());
    }

    @Test
    public void constructor_WithStartTimeNullField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            new LimitedDiscount(null, end);
        });
        assertEquals("Start Time can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEndTimeNullField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 3, 10, 12, 0);
            new LimitedDiscount(start, null);
        });
        assertEquals("End Time can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithInvalidDateRange_ShouldThrowInvalidDateTimeRangeException() {
        InvalidDateTimeRangeException exception = assertThrows(InvalidDateTimeRangeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 3, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 1, 10, 12, 0);
            new LimitedDiscount(start, end);
        });
        assertEquals("StartTime can not be bigger than EndTime", exception.getMessage());
    }

    //------------------------
    @Test
    public void setStartTime_WithStartTimeInPastParameter_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            LimitedDiscount limitedDiscount = new LimitedDiscount(start, end);
            limitedDiscount.setStartTime(LocalDateTime.of(2025, 1, 10, 12, 0));
        });
        assertEquals("Start Time can not be in the past", exception.getMessage());
    }

    @Test
    public void setStartTime_WithValidStartTimeParameter_ShouldChangeStartTime() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
        LimitedDiscount limitedDiscount = new LimitedDiscount(start, end);
        limitedDiscount.setStartTime(LocalDateTime.of(2026, 2, 10, 12, 0));

        assertEquals(start.plusMonths(1), limitedDiscount.getStartTime());
    }

    @Test
    public void setEndTime_WithEndTimeInPastParameter_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            LimitedDiscount limitedDiscount = new LimitedDiscount(start, end);
            limitedDiscount.setEndTime(LocalDateTime.of(2025, 2, 10, 12, 0));
        });
        assertEquals("End Time can not be in the past", exception.getMessage());
    }

    @Test
    public void setStartTime_WithStartTimeNullParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            LimitedDiscount limitedDiscount = new LimitedDiscount(start, end);
            limitedDiscount.setStartTime(null);
        });
        assertEquals("Start Time can not be null", exception.getMessage());
    }

    @Test
    public void setEndTime_WithEndTimeNullParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            LimitedDiscount limitedDiscount = new LimitedDiscount(start, end);
            limitedDiscount.setEndTime(null);
        });
        assertEquals("End Time can not be null", exception.getMessage());
    }

    @Test
    public void setStartTime_WithInvalidDateRange_ShouldThrowInvalidDateTimeRangeException() {
        InvalidDateTimeRangeException exception = assertThrows(InvalidDateTimeRangeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            LimitedDiscount limitedDiscount = new LimitedDiscount(start, end);
            limitedDiscount.setStartTime(LocalDateTime.of(2026, 4, 10, 12, 0));
        });
        assertEquals("StartTime can not be bigger than EndTime", exception.getMessage());
    }

    @Test
    public void setEndTime_WithInvalidDateRange_ShouldThrowInvalidDateTimeRangeException() {
        InvalidDateTimeRangeException exception = assertThrows(InvalidDateTimeRangeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 2, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            LimitedDiscount limitedDiscount = new LimitedDiscount(start, end);
            limitedDiscount.setEndTime(LocalDateTime.of(2026, 1, 10, 12, 0));
        });
        assertEquals("StartTime can not be bigger than EndTime", exception.getMessage());
    }
}
