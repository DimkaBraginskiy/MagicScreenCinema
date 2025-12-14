package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.ContractTypeEnum;
import com.magicscreencinema.domain.exception.InheritanceViolationException;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.*;

import javax.annotation.processing.Generated;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ElementCollection(name = "staffs")
public class Staff{
    private LocalDate hireDate;
    private double salary;
    private ContractTypeEnum contractType;

    @ManyToOne
    private Staff manager;

    @OneToMany(fetch = Fetch.EAGER)
    private final Set<Staff> managedStaff = new HashSet<>();
    @OneToMany(fetch = Fetch.EAGER)
    private final Set<Shift> shifts = new HashSet<>();
    private Person person;

    public Staff(Person person, LocalDate hireDate, double salary, ContractTypeEnum contractType, Staff manager) {
        this(person, hireDate, salary, contractType);
        assignManager(manager);
    }

    public Staff(Person person, LocalDate hireDate, double salary, ContractTypeEnum contractType) {
        this.hireDate = FieldValidator.validateDateNotInTheFuture(hireDate, "Hire Date");
        this.salary = FieldValidator.validatePositiveNumber(salary, "Salary");
        this.contractType = FieldValidator.validateObjectNotNull(contractType, "Contract Type");
        this.person = FieldValidator.validateObjectNotNull(person, "Person");
        if(person.getStaff().isPresent()) {
            throw new InheritanceViolationException("Person is already associated with another Staff.");
        }
    }

    private Staff() {
    }

    //--association logic
    // manager
    public void assignManager(Staff manager) {
        if (this.manager != null) {
            this.manager.removeManagedStaff(this);
        }
        this.manager = FieldValidator.validateObjectRecursion(manager, this);
        this.manager = FieldValidator.validateObjectNotNull(manager, "Manager");

        this.manager.addManagedStaff(this);
    }

    //--association logic
    // staff
    void addManagedStaff(Staff staff) {
        FieldValidator.validateObjectNotNull(staff, "Staff");
        managedStaff.add(staff);
    }

    void removeManagedStaff(Staff staff) {
        FieldValidator.validateObjectNotNull(staff, "Staff");
        managedStaff.remove(staff);
    }

    //--association logic
    // shift
    void addShift(Shift shift) {
        FieldValidator.validateObjectNotNull(shift, "shift");

        if (this.shifts.contains(shift)) {
            return;
        }

        this.shifts.add(shift);
    }

    void removeShift(Shift shift) {
        FieldValidator.validateObjectNotNull(shift, "shift");
        this.shifts.remove(shift);
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

    public Set<Shift> getShifts() {
        return new HashSet<>(shifts);
    }

    public Set<Staff> getManagedStaff() {
        return new HashSet<>(managedStaff);
    }

    public Person getPerson() {
        return person;
    }
}
