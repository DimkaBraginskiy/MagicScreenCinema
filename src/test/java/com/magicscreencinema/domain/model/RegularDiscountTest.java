package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.DuplicateDayException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RegularDiscountTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreateRegularDiscount() {
        RegularDiscount regularDiscount = new RegularDiscount(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY));

        assertNotNull(regularDiscount);
        assertEquals(2, regularDiscount.getDayOfWeek().size());
        assertEquals(DayOfWeek.MONDAY, regularDiscount.getDayOfWeek().get(0));
        assertEquals(DayOfWeek.WEDNESDAY, regularDiscount.getDayOfWeek().get(1));
    }

    @Test
    public void constructor_WithNullDayOfWeekList_ShouldThrowNullArgumentException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new RegularDiscount(null);
        });
        assertEquals("Day Of Week List can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithDayOfWeekListContainingNull_ShouldThrowNullArgumentException() {
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.MONDAY);
        days.add(null);
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new RegularDiscount(days);
        });
        assertEquals("Day Of Week List can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithDayOfWeekListContainingDuplicatedValues_DuplicateDayException() {
        DuplicateDayException exception = assertThrows(DuplicateDayException.class, () -> {
            new RegularDiscount(List.of(DayOfWeek.MONDAY, DayOfWeek.MONDAY));
        });
        assertEquals("Duplicate day 'MONDAY' in Day Of Week List is not allowed", exception.getMessage());
    }

    @Test
    public void setDayOfWeek_WithNullDayOfWeekList_ShouldThrowNullArgumentException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            RegularDiscount regularDiscount = new RegularDiscount(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY));
            regularDiscount.setDayOfWeek(null);
        });
        assertEquals("Day Of Week List can not be null", exception.getMessage());
    }

    @Test
    public void setDayOfWeek_WithDayOfWeekListContainingNull_ShouldThrowNullArgumentException() {
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.MONDAY);
        days.add(null);
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            RegularDiscount regularDiscount = new RegularDiscount(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY));
            regularDiscount.setDayOfWeek(days);
        });
        assertEquals("Day Of Week List can not be null", exception.getMessage());
    }

    @Test
    public void setDayOfWeek_WithDayOfWeekListContainingDuplicatedValues_ShouldThrowDuplicateDayException() {
        DuplicateDayException exception = assertThrows(DuplicateDayException.class, () -> {
            RegularDiscount regularDiscount = new RegularDiscount(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY));
            regularDiscount.setDayOfWeek(List.of(DayOfWeek.MONDAY, DayOfWeek.MONDAY));
        });
        assertEquals("Duplicate day 'MONDAY' in Day Of Week List is not allowed", exception.getMessage());
    }

    @Test
    public void setDayOfWeek_WithValidDayOfWeekList_ShouldChangeDayOfWeekList() {
        RegularDiscount regularDiscount = new RegularDiscount(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY));
        regularDiscount.setDayOfWeek(List.of(DayOfWeek.FRIDAY, DayOfWeek.THURSDAY));

        assertNotNull(regularDiscount);
        assertEquals(2, regularDiscount.getDayOfWeek().size());
        assertEquals(DayOfWeek.FRIDAY, regularDiscount.getDayOfWeek().get(0));
        assertEquals(DayOfWeek.THURSDAY, regularDiscount.getDayOfWeek().get(1));
    }
}
