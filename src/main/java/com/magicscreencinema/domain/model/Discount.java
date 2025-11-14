package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.InvalidDiscountException;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.util.UUID;

@ElementCollection(name = "discounts")
public class Discount {
    @Id
    private UUID id;
    private double discountAmount;
    private String promoCode;

    public Discount(double discountAmount, String promoCode) {
        if (discountAmount > 0 && discountAmount < 1) {
            this.discountAmount = discountAmount;
        } else {
            throw new InvalidDiscountException("Discount Amount must be a decimal value between 0 and 1");
        }

        this.promoCode = FieldValidator.validateNullOrEmptyString(promoCode, "Promo Code");
        id = UUID.randomUUID();
    }

    public Discount() {
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

    public UUID getId() {
        return id;
    }
}
