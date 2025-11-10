package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;

import java.time.LocalDate;

public class Customer extends Person {
    private int loyaltyPoints = 0;

    public Customer(String firstName, String lastName, String phoneNumber, String email, String password, LocalDate birthDate, int loyaltyPoints) {
        super(firstName, lastName, phoneNumber, email, password, birthDate);
        this.loyaltyPoints = FieldValidator.validateNonNegativeNumber(loyaltyPoints, "Loyalty Points");
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = FieldValidator.validateNonNegativeNumber(loyaltyPoints, "Loyalty Points");
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }
}
