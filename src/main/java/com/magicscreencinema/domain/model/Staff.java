package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.ContractTypeEnum;
import com.magicscreencinema.domain.validation.FieldValidator;

import java.time.LocalDate;

public class Staff{
    private LocalDate hireDate;
    private double salary;
    private ContractTypeEnum contractType;

    private Staff manager = null;

    public Staff(LocalDate hireDate, double salary, ContractTypeEnum contractType) {
        this.hireDate = FieldValidator.validateDateNotInTheFuture(hireDate, "Hire Date");
        this.salary = FieldValidator.validatePositiveNumber(salary, "Salary");
        this.contractType = FieldValidator.validateObjectNotNull(contractType, "Contract Type");
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
        this.manager = FieldValidator.validateObjectSelfPassing(manager, this, "Manager", "Staff");
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

    public Staff getManager() {
        return manager;
    }
}
