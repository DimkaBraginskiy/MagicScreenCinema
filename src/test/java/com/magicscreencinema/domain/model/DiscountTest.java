package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.EmptyStringException;
import com.magicscreencinema.domain.exception.InvalidDiscountException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DiscountTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreateDiscount() {
        Discount discount = new Discount(0.6, "XYZ");
        assertEquals(0.6, discount.getDiscountAmount(), 0.001);
        assertEquals("XYZ", discount.getPromoCode());
    }

    @Test
    public void constructor_WithNullPromoCodeField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Discount(0.6, null);
        });
        assertEquals("Promo Code can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyPromoCodeField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new Discount(0.6, "");
        });
        assertEquals("Promo Code can not be empty", exception.getMessage());
    }

    @Test
    public void constructor_WithNegativeDiscountAmountField_ShouldThrowInvalidDiscountException() {
        InvalidDiscountException exception = assertThrows(InvalidDiscountException.class, () -> {
            new Discount(-0.5, "XYZ");
        });
        assertEquals("Discount Amount must be a decimal value between 0 and 1", exception.getMessage());
    }

    @Test
    public void constructor_WithLargePositiveDiscountAmountField_ShouldThrowInvalidDiscountException() {
        InvalidDiscountException exception = assertThrows(InvalidDiscountException.class, () -> {
            new Discount(20, "XYZ");
        });
        assertEquals("Discount Amount must be a decimal value between 0 and 1", exception.getMessage());
    }

    @Test
    public void constructor_WithZeroDiscountAmountField_ShouldThrowInvalidDiscountException() {
        InvalidDiscountException exception = assertThrows(InvalidDiscountException.class, () -> {
            new Discount(0, "XYZ");
        });
        assertEquals("Discount Amount must be a decimal value between 0 and 1", exception.getMessage());
    }

    @Test
    public void setPromoCode_WithNullPromoCodeParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Discount discount = new Discount(0.6, "ZYX");
            discount.setPromoCode(null);
        });
        assertEquals("Promo Code can not be null", exception.getMessage());
    }

    @Test
    public void setPromoCode_WithEmptyPromoCodeParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            Discount discount = new Discount(0.6, "ZYX");
            discount.setPromoCode("");
        });
        assertEquals("Promo Code can not be empty", exception.getMessage());
    }

    @Test
    public void setPromoCode_WithValidPromoCodeParameter_ShouldChangePromoCode() {
        Discount discount = new Discount(0.6, "ZYX");
        discount.setPromoCode("XXX");
        assertEquals("XXX", discount.getPromoCode());
    }

    @Test
    public void setDiscountAmount_WithNegativeDiscountAmountParameter_ShouldThrowInvalidDiscountException() {
        InvalidDiscountException exception = assertThrows(InvalidDiscountException.class, () -> {
            Discount discount = new Discount(0.6, "ZYX");
            discount.setDiscountAmount(-0.1);
        });
        assertEquals("Discount Amount must be a decimal value between 0 and 1", exception.getMessage());
    }

    @Test
    public void setDiscountAmount_WithLargePositiveDiscountAmountField_ShouldThrowInvalidDiscountException() {
        InvalidDiscountException exception = assertThrows(InvalidDiscountException.class, () -> {
            Discount discount = new Discount(0.6, "ZYX");
            discount.setDiscountAmount(20);
        });
        assertEquals("Discount Amount must be a decimal value between 0 and 1", exception.getMessage());
    }

    @Test
    public void setDiscountAmount_WithZeroDiscountAmountField_ShouldThrowInvalidDiscountException() {
        InvalidDiscountException exception = assertThrows(InvalidDiscountException.class, () -> {
            Discount discount = new Discount(0.6, "ZYX");
            discount.setDiscountAmount(0);
        });
        assertEquals("Discount Amount must be a decimal value between 0 and 1", exception.getMessage());
    }

    @Test
    public void setDiscountAmount_WithValidDiscountAmountParameter_ShouldChangeDiscountAmount() {
        Discount discount = new Discount(0.6, "ZYX");
        discount.setDiscountAmount(0.8);
        assertEquals(0.8, discount.getDiscountAmount(), 0.001);
    }
}
