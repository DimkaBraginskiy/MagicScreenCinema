package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.ContractTypeEnum;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@ElementCollection(name = "persons")
public class Person {
    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private LocalDate birthDate;
    private Customer customer;
    private Staff staff;

    private Person(String firstName, String lastName, String phoneNumber, String email, String password, LocalDate birthDate) {
        this.firstName = FieldValidator.validateNullOrEmptyString(firstName, "First Name");
        this.lastName = FieldValidator.validateNullOrEmptyString(lastName, "Last Name");
        this.phoneNumber = FieldValidator.validatePhoneNumber(phoneNumber);
        this.email = FieldValidator.validateEmail(email);
        this.password = FieldValidator.validateNullOrEmptyString(password, "Password");
        this.birthDate = FieldValidator.validateDateNotInTheFuture(birthDate, "Birth Date");
        id = UUID.randomUUID();
    }

    public Person(String firstName, String lastName, String phoneNumber, String email, String password, LocalDate birthDate, int loyaltyPoints){
        this(firstName, lastName, phoneNumber, email, password, birthDate);
        this.customer = new Customer(this, loyaltyPoints);
    }

    public Person(String firstName, String lastName, String phoneNumber, String email, String password, LocalDate birthDate, LocalDate hireDate, double salary, ContractTypeEnum contractType){
        this(firstName, lastName, phoneNumber, email, password, birthDate);
        this.staff = new Staff(this, hireDate, salary, contractType);
    }

    public Person(String firstName, String lastName, String phoneNumber, String email, String password, LocalDate birthDate, LocalDate hireDate, double salary, ContractTypeEnum contractType, Staff manager){
        this(firstName, lastName, phoneNumber, email, password, birthDate);
        this.staff = new Staff(this, hireDate, salary, contractType, manager);
    }

    public Person(String firstName, String lastName, String phoneNumber, String email, String password, LocalDate birthDate, LocalDate hireDate, double salary, ContractTypeEnum contractType, int loyaltyPoints)
    {
        this(firstName, lastName, phoneNumber, email, password, birthDate);
        this.staff = new Staff(this, hireDate, salary, contractType);
        this.customer = new Customer(this, loyaltyPoints);
    }

    public Person(String firstName, String lastName, String phoneNumber, String email, String password, LocalDate birthDate, LocalDate hireDate, double salary, ContractTypeEnum contractType, Staff manager, int loyaltyPoints)
    {
        this(firstName, lastName, phoneNumber, email, password, birthDate);
        this.staff = new Staff(this, hireDate, salary, contractType, manager);
        this.customer = new Customer(this, loyaltyPoints);
    }

    private Person() {
    }

    public void setFirstName(String firstName) {
        this.firstName = FieldValidator.validateNullOrEmptyString(firstName, "First Name");
    }

    public void setLastName(String lastName) {
        this.lastName = FieldValidator.validateNullOrEmptyString(lastName, "Last Name");
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = FieldValidator.validatePhoneNumber(phoneNumber);
    }

    public void setEmail(String email) {
        this.email = FieldValidator.validateEmail(email);
    }

    public void setPassword(String password) {
        this.password = FieldValidator.validateNullOrEmptyString(password, "Password");
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = FieldValidator.validateDateNotInTheFuture(birthDate, "Birth Date");
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Optional<String> getPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public UUID getId() {
        return id;
    }

    public Optional<Customer> getCustomer() {
        return Optional.ofNullable(customer);
    }

    public Optional<Staff> getStaff() {
        return Optional.ofNullable(staff);
    }
}
