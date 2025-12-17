package com.magicscreencinema.domain.inheritance;

import com.magicscreencinema.domain.enums.AgeGroupEnum;
import com.magicscreencinema.domain.exception.EmptyStringException;
import com.magicscreencinema.domain.exception.InvalidDiscountException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import com.magicscreencinema.domain.model.LimitedDiscount;
import com.magicscreencinema.domain.model.RegularDiscount;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
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
        assertTrue(regularDiscount.getAgeGroupDiscount().isPresent());
        assertEquals(AgeGroupEnum.TEENAGER, regularDiscount.getAgeGroupDiscount().get().getGroup());

        // verify Disjoint Constraint (Should NOT have special condition)
        assertFalse(regularDiscount.getSpecialConditionDiscount().isPresent());
    }

    @Test
    public void constructor_WithSpecialConditionParameters_ShouldCreateRegularDiscountWithSpecialCondition() {
        String expectedCondition = "Summer Sale";
        RegularDiscount regularDiscount = new RegularDiscount(
                List.of(DayOfWeek.MONDAY),
                0.1,
                "SUMMER",
                expectedCondition
        );

        // Verify Composition
        assertTrue(regularDiscount.getSpecialConditionDiscount().isPresent());
        assertEquals(expectedCondition, regularDiscount.getSpecialConditionDiscount().get().getConditionDescription());

        // Verify Disjoint Constraint
        assertFalse(regularDiscount.getAgeGroupDiscount().isPresent());
    }

    @Test
    public void constructor_WithNullDescriptionForSpecialCondition_ShouldThrowException() {
        NullAttributeException exception = assertThrows(
                NullAttributeException.class,
                () -> new RegularDiscount(
                        List.of(DayOfWeek.MONDAY),
                        0.1,
                        "SUMMER",
                        null
                )
        );

        assertEquals("Condition Description can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyDescriptionForSpecialCondition_ShouldThrowException() {
        EmptyStringException exception = assertThrows(
                EmptyStringException.class,
                () -> new RegularDiscount(
                        List.of(DayOfWeek.MONDAY),
                        0.1,
                        "SUMMER",
                        ""
                )
        );

        assertEquals("Condition Description can not be empty", exception.getMessage());
    }

    @Test
    public void constructor_WithNullDescriptionForAgeGroup_ShouldThrowException() {
        NullAttributeException exception = assertThrows(
                NullAttributeException.class,
                () -> new RegularDiscount(
                        List.of(DayOfWeek.MONDAY),
                        0.1,
                        "SUMMER",
                        null,
                        AgeGroupEnum.TEENAGER
                )
        );

        assertEquals("Description can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithNullGroupFieldForAgeGroup_ShouldThrowException() {
        NullAttributeException exception = assertThrows(
                NullAttributeException.class,
                () -> new RegularDiscount(
                        List.of(DayOfWeek.MONDAY),
                        0.1,
                        "SUMMER",
                        "descr",
                        null
                )
        );

        assertEquals("Group can not be null", exception.getMessage());
    }
}
