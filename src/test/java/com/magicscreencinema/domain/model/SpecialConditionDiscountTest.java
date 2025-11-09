package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.EmptyStringException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SpecialConditionDiscountTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreateGenre() {
        SpecialConditionDiscount discount = new SpecialConditionDiscount("Test condition");
        assertEquals("Test condition", discount.getConditionDescription());
    }

    @Test
    public void constructor_WithNullConditionDescriptionField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new SpecialConditionDiscount(null);
        });
        assertEquals("Condition Description can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyConditionDescriptionField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new SpecialConditionDiscount("");
        });
        assertEquals("Condition Description can not be empty", exception.getMessage());
    }

    @Test
    public void setConditionDescription_WithNullConditionDescriptionParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            SpecialConditionDiscount discount = new SpecialConditionDiscount("Test condition");
            discount.setConditionDescription(null);
        });
        assertEquals("Condition Description can not be null", exception.getMessage());
    }

    @Test
    public void setConditionDescription_WithEmptyConditionDescriptionParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            SpecialConditionDiscount discount = new SpecialConditionDiscount("Test condition");
            discount.setConditionDescription("");
        });
        assertEquals("Condition Description can not be empty", exception.getMessage());
    }

    @Test
    public void setConditionDescription_WithValidConditionDescriptionParameter_ShouldChangeConditionDescription() {
        SpecialConditionDiscount discount = new SpecialConditionDiscount("Test condition");
        discount.setConditionDescription("New condition description");

        assertEquals("New condition description", discount.getConditionDescription());
    }
}
