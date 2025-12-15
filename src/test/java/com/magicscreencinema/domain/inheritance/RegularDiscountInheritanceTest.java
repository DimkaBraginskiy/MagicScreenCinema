package com.magicscreencinema.domain.inheritance;

import com.magicscreencinema.domain.enums.AgeGroupEnum;
import com.magicscreencinema.domain.exception.InvalidDiscountException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import com.magicscreencinema.domain.model.RegularDiscount;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class RegularDiscountInheritanceTest {
    @Test
    public void constructor_WithAgeGroupParameters_ShouldCreateRegularDiscountWithAgeGroup() {
        RegularDiscount regularDiscount = new RegularDiscount(
                List.of(DayOfWeek.FRIDAY),
                0.15,
                "STUDENT_FRI",
                "Student Friday",
                AgeGroupEnum.TEENAGER
        );

        assertNotNull(regularDiscount);
        assertEquals(1, regularDiscount.getDayOfWeek().size());
        assertEquals(DayOfWeek.FRIDAY, regularDiscount.getDayOfWeek().get(0));

        // verify Composition (Inherited from Discount)
        assertTrue("AgeGroupDiscount should be present", regularDiscount.getAgeGroupDiscount().isPresent());
        assertEquals(AgeGroupEnum.TEENAGER, regularDiscount.getAgeGroupDiscount().get().getGroup());

        // verify Disjoint Constraint (Should NOT have special condition)
        assertFalse("SpecialConditionDiscount should NOT be present", regularDiscount.getSpecialConditionDiscount().isPresent());
    }

    @Test
    public void constructor_WithSpecialConditionParameters_ShouldCheckInheritanceAndComposition() {
        String expectedCondition = "Summer Sale";
        RegularDiscount regularDiscount = new RegularDiscount(
                List.of(DayOfWeek.MONDAY),
                0.1,
                "SUMMER",
                expectedCondition
        );

        // Verify Composition
        assertTrue("SpecialConditionDiscount should be present", regularDiscount.getSpecialConditionDiscount().isPresent());
        assertEquals(expectedCondition, regularDiscount.getSpecialConditionDiscount().get().getConditionDescription());

        // Verify Disjoint Constraint
        assertFalse("AgeGroupDiscount should NOT be present", regularDiscount.getAgeGroupDiscount().isPresent());
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
