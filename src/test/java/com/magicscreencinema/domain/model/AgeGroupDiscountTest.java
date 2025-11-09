package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.AgeGroupEnum;
import com.magicscreencinema.domain.exception.EmptyStringException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class AgeGroupDiscountTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreateAgeGroupDiscount() {
        AgeGroupDiscount discount = new AgeGroupDiscount("Test Description", AgeGroupEnum.KID);
        assertEquals("Test Description", discount.getDescription());
        assertEquals(AgeGroupEnum.KID, discount.getGroup());
    }

    @Test
    public void constructor_WithNullDescriptionField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new AgeGroupDiscount(null, AgeGroupEnum.KID);
        });
        assertEquals("Description can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyDescriptionField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new AgeGroupDiscount("", AgeGroupEnum.KID);
        });
        assertEquals("Description can not be empty", exception.getMessage());
    }

    @Test
    public void constructor_WithNullGroupField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new AgeGroupDiscount("Description", null);
        });
        assertEquals("Group can not be null", exception.getMessage());
    }

    @Test
    public void setDescription_WithNullDescriptionParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            AgeGroupDiscount discount = new AgeGroupDiscount("Description", AgeGroupEnum.KID);
            discount.setDescription(null);
        });
        assertEquals("Description can not be null", exception.getMessage());
    }

    @Test
    public void setDescription_WithEmptyDescriptionParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            AgeGroupDiscount discount = new AgeGroupDiscount("Description", AgeGroupEnum.KID);
            discount.setDescription("");
        });
        assertEquals("Description can not be empty", exception.getMessage());
    }

    @Test
    public void setDescription_WithValidDescriptionParameter_ShouldChangeDescriptionField() {
        AgeGroupDiscount discount = new AgeGroupDiscount("Description", AgeGroupEnum.KID);
        discount.setDescription("New Description");

        assertEquals("New Description", discount.getDescription());
    }

    @Test
    public void setGroup_WithNullGroupParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            AgeGroupDiscount discount = new AgeGroupDiscount("Description", AgeGroupEnum.KID);
            discount.setGroup(null);
        });
        assertEquals("Group can not be null", exception.getMessage());
    }

    @Test
    public void setGroup_WithValidGroupParameter_ShouldChangeGroupField() {
        AgeGroupDiscount discount = new AgeGroupDiscount("Description", AgeGroupEnum.KID);
        discount.setGroup(AgeGroupEnum.TEENAGER);

        assertEquals(AgeGroupEnum.TEENAGER, discount.getGroup());
    }
}
