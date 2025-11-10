package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.ContractTypeEnum;
import com.magicscreencinema.domain.validation.FieldValidator;

import java.time.LocalDate;
import java.util.Optional;

public class Staff extends Person {
    private LocalDate hireDate;
    private double salary;
    private ContractTypeEnum contractType;

    private Staff manager;

    public Staff(String firstName, String lastName, String phoneNumber, String email, String password,
                 LocalDate birthDate, LocalDate hireDate, double salary, ContractTypeEnum contractType, Staff manager) {
        super(firstName, lastName, phoneNumber, email, password, birthDate);
        this.hireDate = FieldValidator.validateDateNotInTheFuture(hireDate, "Hire Date");
        this.salary = FieldValidator.validatePositiveNumber(salary, "Salary");
        this.contractType = FieldValidator.validateObjectNotNull(contractType, "Contract Type");
        this.manager = FieldValidator.validateObjectRecursion(manager, this);
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = FieldValidator.validateDateNotInTheFuture(hireDate, "Hire Date");
    }

    public void setSalary(double salary) {
        this.salary = FieldValidator.validatePositiveNumber(salary, "Salary");
    }

    public void setContractType(ContractTypeEnum contractType) {
        this.contractType = FieldValidator.validateObjectNotNull(contractType, "Contract Type");
    }

    public void setManager(Staff manager) {
        this.manager = FieldValidator.validateObjectRecursion(manager, this);
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public ContractTypeEnum getContractType() {
        return contractType;
    }

    public Optional<Staff> getManager() {
        return Optional.ofNullable(manager);
    }
}
