package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.AgeGroupEnum;
import com.magicscreencinema.domain.exception.*;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class LimitedDiscountTest {
    private final LocalDateTime validStart = LocalDateTime.now().plusDays(1);
    private final LocalDateTime validEnd = LocalDateTime.now().plusDays(5);

    @Test
    public void constructor_WithValidParameters_ShouldCreateLimitedDiscount() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
        LimitedDiscount discount = new LimitedDiscount(start, end, 0.1, "SDFDFSD", "descr");

        assertNotNull(discount);
        assertEquals(start, discount.getStartTime());
        assertEquals(end, discount.getEndTime());
    }

    @Test
    public void constructor_WithStartTimeInPastField_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2025, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            new LimitedDiscount(start, end, 0.1, "SDFDFSD", "descr");
        });
        assertEquals("Start Time can not be in the past", exception.getMessage());
    }

    @Test
    public void constructor_WithEndTimeInPastField_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2025, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2025, 3, 10, 12, 0);
            new LimitedDiscount(start, end, 0.1, "SDFDFSD", "descr");
        });
        assertEquals("End Time can not be in the past", exception.getMessage());
    }

    @Test
    public void constructor_WithStartTimeNullField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            new LimitedDiscount(null, end, 0.1, "SDFDFSD", "descr");
        });
        assertEquals("Start Time can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEndTimeNullField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 3, 10, 12, 0);
            new LimitedDiscount(start, null, 0.1, "SDFDFSD", "descr");
        });
        assertEquals("End Time can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithInvalidDateRange_ShouldThrowInvalidDateTimeRangeException() {
        InvalidDateTimeRangeException exception = assertThrows(InvalidDateTimeRangeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 3, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 1, 10, 12, 0);
            new LimitedDiscount(start, end, 0.1, "SDFDFSD", "descr");
        });
        assertEquals("StartTime can not be bigger than EndTime", exception.getMessage());
    }

    //------------------------
    @Test
    public void setStartTime_WithStartTimeInPastParameter_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            LimitedDiscount limitedDiscount = new LimitedDiscount(start, end, 0.1, "SDFDFSD", "descr");
            limitedDiscount.setStartTime(LocalDateTime.of(2025, 1, 10, 12, 0));
        });
        assertEquals("Start Time can not be in the past", exception.getMessage());
    }

    @Test
    public void setStartTime_WithValidStartTimeParameter_ShouldChangeStartTime() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
        LimitedDiscount limitedDiscount = new LimitedDiscount(start, end, 0.1, "SDFDFSD", "descr");
        limitedDiscount.setStartTime(LocalDateTime.of(2026, 2, 10, 12, 0));

        assertEquals(start.plusMonths(1), limitedDiscount.getStartTime());
    }

    @Test
    public void setEndTime_WithEndTimeInPastParameter_ShouldThrowDateInPastException() {
        DateInPastException exception = assertThrows(DateInPastException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            LimitedDiscount limitedDiscount = new LimitedDiscount(start, end, 0.1, "SDFDFSD", "descr");
            limitedDiscount.setEndTime(LocalDateTime.of(2025, 2, 10, 12, 0));
        });
        assertEquals("End Time can not be in the past", exception.getMessage());
    }

    @Test
    public void setStartTime_WithStartTimeNullParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            LimitedDiscount limitedDiscount = new LimitedDiscount(start, end, 0.1, "SDFDFSD", "descr");
            limitedDiscount.setStartTime(null);
        });
        assertEquals("Start Time can not be null", exception.getMessage());
    }

    @Test
    public void setEndTime_WithEndTimeNullParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            LimitedDiscount limitedDiscount = new LimitedDiscount(start, end, 0.1, "SDFDFSD", "descr");
            limitedDiscount.setEndTime(null);
        });
        assertEquals("End Time can not be null", exception.getMessage());
    }

    @Test
    public void setStartTime_WithInvalidDateRange_ShouldThrowInvalidDateTimeRangeException() {
        InvalidDateTimeRangeException exception = assertThrows(InvalidDateTimeRangeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            LimitedDiscount limitedDiscount = new LimitedDiscount(start, end, 0.1, "SDFDFSD", "descr");
            limitedDiscount.setStartTime(LocalDateTime.of(2026, 4, 10, 12, 0));
        });
        assertEquals("StartTime can not be bigger than EndTime", exception.getMessage());
    }

    @Test
    public void setEndTime_WithInvalidDateRange_ShouldThrowInvalidDateTimeRangeException() {
        InvalidDateTimeRangeException exception = assertThrows(InvalidDateTimeRangeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 2, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            LimitedDiscount limitedDiscount = new LimitedDiscount(start, end, 0.1, "SDFDFSD", "descr");
            limitedDiscount.setEndTime(LocalDateTime.of(2026, 1, 10, 12, 0));
        });
        assertEquals("StartTime can not be bigger than EndTime", exception.getMessage());
    }

    // inheritance
    @Test
    public void constructor_WithAgeGroupParameters_ShouldCreateLimitedDiscountWithAgeGroup() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);

        LimitedDiscount discount = new LimitedDiscount(
                start,
                end,
                0.2,
                "SENIOR_SUMMER",
                "Senior Special",
                AgeGroupEnum.SENIOR
        );

        assertNotNull(discount);
        assertEquals(start, discount.getStartTime());

        // verify Abstract Discount fields
        assertEquals(0.2, discount.getDiscountAmount(), 0.0);
        assertEquals("SENIOR_SUMMER", discount.getPromoCode());

        // verify Composition (Inherited from Discount)
        assertTrue("AgeGroupDiscount should be present", discount.getAgeGroupDiscount().isPresent());
        assertEquals(AgeGroupEnum.SENIOR, discount.getAgeGroupDiscount().get().getGroup());

        // verify Disjoint Constraint (should not have special condition)
        assertFalse("SpecialConditionDiscount should NOT be present", discount.getSpecialConditionDiscount().isPresent());
    }

    @Test
    public void constructor_WithSpecialConditionParameters_ShouldCheckInheritanceAndComposition() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
        String expectedPromo = "VIP_SUMMER";
        String expectedCondition = "VIP Members Only";
        double expectedAmount = 0.15;

        LimitedDiscount discount = new LimitedDiscount(
                start,
                end,
                expectedAmount,
                expectedPromo,
                expectedCondition
        );
        assertEquals(start, discount.getStartTime());
        assertEquals(end, discount.getEndTime());
        assertEquals(expectedAmount, discount.getDiscountAmount(), 0.0);
        assertEquals(expectedPromo, discount.getPromoCode());

        // verify Parent Composition (SpecialConditionDiscount) is created
        assertTrue("SpecialCondition should be present", discount.getSpecialConditionDiscount().isPresent());
        assertEquals(expectedCondition, discount.getSpecialConditionDiscount().get().getConditionDescription());

        // verify Disjoint Constraint (Should NOT have AgeGroup)
        assertFalse("AgeGroup should NOT be present", discount.getAgeGroupDiscount().isPresent());
    }

    @Test
    public void constructor_WithZeroDiscountAmount_ShouldThrowInvalidDiscountException() {
        InvalidDiscountException exception = assertThrows(InvalidDiscountException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            // 0.0 is invalid (must be > 0 and < 1)
            new LimitedDiscount(start, end, 0.0, "PROMO", "descr");
        });
        assertEquals("Discount Amount must be a decimal value between 0 and 1", exception.getMessage());
    }

    @Test
    public void constructor_WithDiscountAmountOne_ShouldThrowInvalidDiscountException() {
        InvalidDiscountException exception = assertThrows(InvalidDiscountException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            // 1.0 is invalid (must be < 1)
            new LimitedDiscount(start, end, 1.0, "PROMO", "descr");
        });
        assertEquals("Discount Amount must be a decimal value between 0 and 1", exception.getMessage());
    }

    @Test
    public void constructor_WithNegativeDiscountAmount_ShouldThrowInvalidDiscountException() {
        assertThrows(InvalidDiscountException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
            new LimitedDiscount(start, end, -0.5, "PROMO", "descr");
        });
    }

    @Test
    public void constructor_WithNullPromoCode_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
            LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);

            new LimitedDiscount(start, end, 0.5, null, "descr");
        });
        assertEquals("Promo Code can not be null", exception.getMessage());
    }

    @Test
    public void setDiscountAmount_WithValidAmount_ShouldUpdateInheritedField() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
        LimitedDiscount discount = new LimitedDiscount(start, end, 0.1, "PROMO", "descr");

        discount.setDiscountAmount(0.5);

        assertEquals(0.5, discount.getDiscountAmount(), 0.0);
    }

    @Test
    public void setPromoCode_WithValidString_ShouldUpdateInheritedField() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);
        LimitedDiscount discount = new LimitedDiscount(start, end, 0.1, "OLD_CODE", "descr");

        discount.setPromoCode("NEW_CODE");

        assertEquals("NEW_CODE", discount.getPromoCode());
    }
}
