package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;

import java.time.LocalDate;
import java.util.Optional;

public abstract class Person {
    protected String firstName;
    protected String lastName;
    protected String phoneNumber;
    protected String email;
    protected String password;
    protected LocalDate birthDate;

    protected Person(String firstName, String lastName, String phoneNumber, String email, String password, LocalDate birthDate) {
        this.firstName = FieldValidator.validateNullOrEmptyString(firstName, "First Name");
        this.lastName = FieldValidator.validateNullOrEmptyString(lastName, "Last Name");
        this.phoneNumber = FieldValidator.validateNullableString(phoneNumber, "Phone Number");
        this.email = FieldValidator.validateNullOrEmptyString(email, "Email");
        this.password = FieldValidator.validateNullOrEmptyString(password, "Password");
        FieldValidator.validateDateNotInTheFuture(birthDate, "Birth Date");
        this.birthDate = FieldValidator.validateDateNotInThePast(birthDate, "Birth Date");
    }

    protected void setFirstName(String firstName) {
        this.firstName = FieldValidator.validateNullOrEmptyString(firstName, "First Name");
    }

    protected void setLastName(String lastName) {
        this.lastName = FieldValidator.validateNullOrEmptyString(lastName, "Last Name");
    }

    protected void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = FieldValidator.validateNullableString(phoneNumber, "Phone Number");
    }

    protected void setEmail(String email) {
        this.email = FieldValidator.validateNullOrEmptyString(email, "Email");
    }

    protected void setPassword(String password) {
        this.password = FieldValidator.validateNullOrEmptyString(password, "Password");
    }

    protected void setBirthDate(LocalDate birthDate) {
        FieldValidator.validateDateNotInTheFuture(birthDate, "Birth Date");
        this.birthDate = FieldValidator.validateDateNotInThePast(birthDate, "Birth Date");
    }

    protected String getFirstName() {
        return firstName;
    }

    protected String getLastName() {
        return lastName;
    }

    protected Optional<String> getPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    protected String getEmail() {
        return email;
    }

    protected String getPassword() {
        return password;
    }

    protected LocalDate getBirthDate() {
        return birthDate;
    }
}
