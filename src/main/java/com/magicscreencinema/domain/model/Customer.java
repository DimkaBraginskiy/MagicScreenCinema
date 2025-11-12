package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.time.LocalDate;
import java.util.UUID;

@ElementCollection(name = "customers")
public class Customer extends Person {
    @Id
    private UUID id;
    private int loyaltyPoints = 0;

    public Customer(String firstName, String lastName, String phoneNumber, String email, String password, LocalDate birthDate, int loyaltyPoints) {
        super(firstName, lastName, phoneNumber, email, password, birthDate);
        this.loyaltyPoints = FieldValidator.validateNonNegativeNumber(loyaltyPoints, "Loyalty Points");
    }

    public Customer(String firstName, String lastName, String phoneNumber, String email, String password, LocalDate birthDate) {
        super(firstName, lastName, phoneNumber, email, password, birthDate);
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = FieldValidator.validateNonNegativeNumber(loyaltyPoints, "Loyalty Points");
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }
}
