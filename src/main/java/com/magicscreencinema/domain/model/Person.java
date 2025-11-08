package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.EmptyStringException;
import com.magicscreencinema.domain.exception.NullAttributeException;
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
        this.firstName = FieldValidator.validateNullOrEmpty(firstName, "First Name");
        this.lastName = FieldValidator.validateNullOrEmpty(lastName, "Last Name");
        this.phoneNumber = FieldValidator.validateNullable(phoneNumber, "Phone Number");
        this.email = FieldValidator.validateNullOrEmpty(email, "Email");
        this.password = FieldValidator.validateNullOrEmpty(password, "Password");
        this.birthDate = FieldValidator.validateDate(birthDate, "Birth Date");
    }

    public abstract Person register();

    protected void setFirstName(String firstName) {
        this.firstName = FieldValidator.validateNullOrEmpty(firstName, "First Name");
    }

    protected void setLastName(String lastName) {
        this.lastName = FieldValidator.validateNullOrEmpty(lastName, "Last Name");
    }

    protected void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = FieldValidator.validateNullable(phoneNumber, "Phone Number");
    }

    protected void setEmail(String email) {
        this.email = FieldValidator.validateNullOrEmpty(email, "Email");
    }

    protected void setPassword(String password) {
        this.password = FieldValidator.validateNullOrEmpty(password, "Password");
    }

    protected void setBirthDate(LocalDate birthDate) {
        this.birthDate = FieldValidator.validateDate(birthDate, "Birth Date");
    }

    protected String getProfile(){
        return "First Name: " + firstName +
                "\nLast Name" + lastName +
                "\nPhone Number: " + (phoneNumber != null ? phoneNumber : "N/A") +
                "\nEmail: " + email +
                "\nBirth Date: " + birthDate.toString();
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
