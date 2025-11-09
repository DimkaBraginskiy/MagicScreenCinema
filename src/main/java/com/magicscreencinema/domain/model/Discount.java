package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.InvalidDiscountException;
import com.magicscreencinema.domain.validation.FieldValidator;

public class Discount {
    private double discountAmount;
    private String promoCode;

    public Discount(double discountAmount, String promoCode) {
        if (discountAmount > 0 && discountAmount < 1) {
            this.discountAmount = discountAmount;
        } else {
            throw new InvalidDiscountException("Discount Amount must be a decimal value between 0 and 1");
        }

        this.promoCode = FieldValidator.validateNullOrEmptyString(promoCode, "Promo Code");
    }

    public void setDiscountAmount(double discountAmount) {
        if (discountAmount > 0 && discountAmount < 1) {
            this.discountAmount = discountAmount;
        } else {
            throw new InvalidDiscountException("Discount Amount must be a decimal value between 0 and 1");
        }
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = FieldValidator.validateNullOrEmptyString(promoCode, "Promo Code");
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public String getPromoCode() {
        return promoCode;
    }
}
