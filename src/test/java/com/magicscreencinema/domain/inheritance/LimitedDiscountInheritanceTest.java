package com.magicscreencinema.domain.inheritance;

import com.magicscreencinema.domain.enums.AgeGroupEnum;
import com.magicscreencinema.domain.exception.EmptySeatListException;
import com.magicscreencinema.domain.exception.EmptyStringException;
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
        assertTrue(discount.getAgeGroupDiscount().isPresent());
        assertEquals(AgeGroupEnum.SENIOR, discount.getAgeGroupDiscount().get().getGroup());

        // verify Disjoint Constraint (should not have special condition)
        assertFalse(discount.getSpecialConditionDiscount().isPresent());
    }

    @Test
    public void constructor_WithSpecialConditionParameters_ShouldCreateLimitedDiscountWithSpecialCondition() {
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
        assertTrue(discount.getSpecialConditionDiscount().isPresent());
        assertEquals(expectedCondition, discount.getSpecialConditionDiscount().get().getConditionDescription());

        // verify Disjoint Constraint (Should NOT have AgeGroup)
        assertFalse(discount.getAgeGroupDiscount().isPresent());
    }

    @Test
    public void constructor_WithNullDescriptionForSpecialCondition_ShouldThrowException() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);

        NullAttributeException exception = assertThrows(
                NullAttributeException.class,
                () -> new LimitedDiscount(
                        start,
                        end,
                        0.2,
                        "SENIOR_SUMMER",
                        null
                )
        );

        assertEquals("Condition Description can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyDescriptionForSpecialCondition_ShouldThrowException() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);

        EmptyStringException exception = assertThrows(
                EmptyStringException.class,
                () -> new LimitedDiscount(
                        start,
                        end,
                        0.2,
                        "SENIOR_SUMMER",
                        ""
                )
        );

        assertEquals("Condition Description can not be empty", exception.getMessage());
    }

    @Test
    public void constructor_WithNullDescriptionForAgeGroup_ShouldThrowException() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);

        NullAttributeException exception = assertThrows(
                NullAttributeException.class,
                () -> new LimitedDiscount(
                        start,
                        end,
                        0.2,
                        "SENIOR_SUMMER",
                        null,
                        AgeGroupEnum.SENIOR
                )
        );

        assertEquals("Description can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithNullGroupFieldForAgeGroup_ShouldThrowException() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 10, 12, 0);
        LocalDateTime end = LocalDateTime.of(2026, 3, 10, 12, 0);

        NullAttributeException exception = assertThrows(
                NullAttributeException.class,
                () -> new LimitedDiscount(
                        start,
                        end,
                        0.2,
                        "SENIOR_SUMMER",
                        "desc",
                        null
                )
        );

        assertEquals("Group can not be null", exception.getMessage());
    }
}
