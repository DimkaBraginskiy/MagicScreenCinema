package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;

import java.time.LocalDate;

public class Customer{
    private int loyaltyPoints = 0;

    public Customer(int loyaltyPoints) {
        this.loyaltyPoints = FieldValidator.validatePositiveNumber(loyaltyPoints, "Loyalty Points");
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = FieldValidator.validatePositiveNumber(loyaltyPoints, "Loyalty Points");
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }
}
