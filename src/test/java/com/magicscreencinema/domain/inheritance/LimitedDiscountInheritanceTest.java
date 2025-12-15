package com.magicscreencinema.domain.inheritance;

import com.magicscreencinema.domain.enums.AgeGroupEnum;
import com.magicscreencinema.domain.exception.InvalidDiscountException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import com.magicscreencinema.domain.model.LimitedDiscount;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class LimitedDiscountInheritanceTest {
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
