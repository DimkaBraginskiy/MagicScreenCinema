package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.ContractTypeEnum;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;
import com.magicscreencinema.persistence.declaration.ManyToOne;
import com.magicscreencinema.persistence.declaration.OneToMany;

import javax.annotation.processing.Generated;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ElementCollection(name = "staffs")
public class Staff extends Person {
    private LocalDate hireDate;
    private double salary;
    private ContractTypeEnum contractType;

    @ManyToOne
    private Staff manager;

    @OneToMany
    private Set<Staff> managedStaff;

    private Set<Shift> shifts;

    public Staff(String firstName, String lastName, String phoneNumber, String email, String password,
                 LocalDate birthDate, LocalDate hireDate, double salary, ContractTypeEnum contractType, Staff manager) {
        super(firstName, lastName, phoneNumber, email, password, birthDate);
        this.hireDate = FieldValidator.validateDateNotInTheFuture(hireDate, "Hire Date");
        this.salary = FieldValidator.validatePositiveNumber(salary, "Salary");
        this.contractType = FieldValidator.validateObjectNotNull(contractType, "Contract Type");


        this.shifts = new HashSet<>();
        this.managedStaff = new HashSet<>();

        assignManager(manager);
    }

    public Staff(String firstName, String lastName, String phoneNumber, String email, String password, LocalDate birthDate) {
        super(firstName, lastName, phoneNumber, email, password, birthDate);
    }
    private Staff() {
    }

    //--association logic
    // manager
    public void assignManager(Staff manager){
        if(this.manager != null){
            this.manager.removeManagedStaff(this);
        }

        this.manager = FieldValidator.validateObjectNotNull(manager, "manager");

        this.manager.addManagedStaff(this);
    }

    //--association logic
    // staff
    void addManagedStaff(Staff staff){
        FieldValidator.validateObjectNotNull(staff, "staff");
        managedStaff.add(staff);
    }

    void removeManagedStaff(Staff staff){
        FieldValidator.validateObjectNotNull(staff, "staff");
        managedStaff.remove(staff);
    }

    //--association logic
    // shift
    void addShift(Shift shift){
        FieldValidator.validateObjectNotNull(shift, "shift");

        if(this.shifts.contains(shift)){
            return;
        }

        this.shifts.add(shift);
    }

    void removeShift(Shift shift){
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
    public Set<Shift> getShifts(){
        return new HashSet<>(shifts);
    }

    public Set<Staff> getManagedStaff(){
        return new HashSet<>(managedStaff);
    }
}
