package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.AgeGroupEnum;
import com.magicscreencinema.domain.exception.DuplicateDayException;
import com.magicscreencinema.domain.exception.InvalidDiscountException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RegularDiscountTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreateRegularSpecialConditionDiscount() {
        RegularDiscount regularDiscount = new RegularDiscount(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), 0.1,
                "SDFDFSD", "descr");

        assertNotNull(regularDiscount);
        assertEquals(2, regularDiscount.getDayOfWeek().size());
        assertEquals(DayOfWeek.MONDAY, regularDiscount.getDayOfWeek().get(0));
        assertEquals(DayOfWeek.WEDNESDAY, regularDiscount.getDayOfWeek().get(1));
        assertEquals("descr", regularDiscount.getSpecialConditionDiscount().get().getConditionDescription());
        assertTrue(regularDiscount.getAgeGroupDiscount().isEmpty());
    }

    @Test
    public void constructor_WithValidParameters_ShouldCreateRegularAgeGroupDiscount() {
        RegularDiscount regularDiscount = new RegularDiscount(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), 0.1,
                "SDFDFSD", "descr", AgeGroupEnum.KID);

        assertNotNull(regularDiscount);
        assertEquals(2, regularDiscount.getDayOfWeek().size());
        assertEquals(DayOfWeek.MONDAY, regularDiscount.getDayOfWeek().get(0));
        assertEquals(DayOfWeek.WEDNESDAY, regularDiscount.getDayOfWeek().get(1));
        assertEquals("descr", regularDiscount.getAgeGroupDiscount().get().getDescription());
        assertEquals(AgeGroupEnum.KID, regularDiscount.getAgeGroupDiscount().get().getGroup());
        assertTrue(regularDiscount.getSpecialConditionDiscount().isEmpty());
    }

    @Test
    public void constructor_WithNullDayOfWeekList_ShouldThrowNullArgumentException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new RegularDiscount(null, 0.1, "SDFDFSD", "descr");
        });
        assertEquals("Day Of Week List can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithDayOfWeekListContainingNull_ShouldThrowNullArgumentException() {
        ArrayList<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.MONDAY);
        days.add(null);
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new RegularDiscount(days, 0.1, "SDFDFSD", "descr");
        });
        assertEquals("Day Of Week List can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithDayOfWeekListContainingDuplicatedValues_DuplicateDayException() {
        DuplicateDayException exception = assertThrows(DuplicateDayException.class, () -> {
            new RegularDiscount(List.of(DayOfWeek.MONDAY, DayOfWeek.MONDAY), 0.1, "SDFDFSD", "descr");
        });
        assertEquals("Duplicate day 'MONDAY' in Day Of Week List is not allowed", exception.getMessage());
    }

    @Test
    public void setDayOfWeek_WithNullDayOfWeekList_ShouldThrowNullArgumentException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            RegularDiscount regularDiscount = new RegularDiscount(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), 0.1, "SDFDFSD", "descr");
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
            RegularDiscount regularDiscount = new RegularDiscount(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), 0.1, "SDFDFSD", "descr");
            regularDiscount.setDayOfWeek(days);
        });
        assertEquals("Day Of Week List can not be null", exception.getMessage());
    }

    @Test
    public void setDayOfWeek_WithDayOfWeekListContainingDuplicatedValues_ShouldThrowDuplicateDayException() {
        DuplicateDayException exception = assertThrows(DuplicateDayException.class, () -> {
            RegularDiscount regularDiscount = new RegularDiscount(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), 0.1, "SDFDFSD", "descr");
            regularDiscount.setDayOfWeek(List.of(DayOfWeek.MONDAY, DayOfWeek.MONDAY));
        });
        assertEquals("Duplicate day 'MONDAY' in Day Of Week List is not allowed", exception.getMessage());
    }

    @Test
    public void setDayOfWeek_WithValidDayOfWeekList_ShouldChangeDayOfWeekList() {
        RegularDiscount regularDiscount = new RegularDiscount(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), 0.1, "SDFDFSD", "descr");
        regularDiscount.setDayOfWeek(List.of(DayOfWeek.FRIDAY, DayOfWeek.THURSDAY));

        assertNotNull(regularDiscount);
        assertEquals(2, regularDiscount.getDayOfWeek().size());
        assertEquals(DayOfWeek.FRIDAY, regularDiscount.getDayOfWeek().get(0));
        assertEquals(DayOfWeek.THURSDAY, regularDiscount.getDayOfWeek().get(1));
    }

    @Test
    public void constructor_WithZeroDiscountAmount_ShouldThrowInvalidDiscountException() {
        InvalidDiscountException exception = assertThrows(InvalidDiscountException.class, () -> {
            // 0.0 is invalid (must be > 0 and < 1)
            new RegularDiscount(List.of(DayOfWeek.MONDAY), 0.0, "PROMO", "descr");
        });
        assertEquals("Discount Amount must be a decimal value between 0 and 1", exception.getMessage());
    }

    @Test
    public void constructor_WithDiscountAmountOne_ShouldThrowInvalidDiscountException() {
        InvalidDiscountException exception = assertThrows(InvalidDiscountException.class, () -> {
            // 1.0 is invalid (must be < 1)
            new RegularDiscount(List.of(DayOfWeek.MONDAY), 1.0, "PROMO", "descr");
        });
        assertEquals("Discount Amount must be a decimal value between 0 and 1", exception.getMessage());
    }

    @Test
    public void constructor_WithNegativeDiscountAmount_ShouldThrowInvalidDiscountException() {
        assertThrows(InvalidDiscountException.class, () -> {
            new RegularDiscount(List.of(DayOfWeek.MONDAY), -0.5, "PROMO", "descr");
        });
    }

    @Test
    public void constructor_WithNullPromoCode_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new RegularDiscount(List.of(DayOfWeek.MONDAY), 0.5, null, "descr");
        });
        assertEquals("Promo Code can not be null", exception.getMessage());
    }

    @Test
    public void setDiscountAmount_WithValidAmount_ShouldUpdateInheritedField() {
        RegularDiscount discount = new RegularDiscount(List.of(DayOfWeek.MONDAY), 0.1, "PROMO", "descr");

        discount.setDiscountAmount(0.5);

        assertEquals(0.5, discount.getDiscountAmount(), 0.0);
    }

    @Test
    public void setPromoCode_WithValidString_ShouldUpdateInheritedField() {
        RegularDiscount discount = new RegularDiscount(List.of(DayOfWeek.MONDAY), 0.1, "OLD_CODE", "descr");

        discount.setPromoCode("NEW_CODE");

        assertEquals("NEW_CODE", discount.getPromoCode());
    }
}
