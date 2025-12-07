package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;
import com.magicscreencinema.persistence.declaration.ManyToOne;

import java.time.LocalDateTime;
import java.util.UUID;

@ElementCollection(name = "shifts")
public class Shift {
    @Id
    private UUID id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @ManyToOne
    private Hall hall;
    @ManyToOne
    private Staff staff;

    public Shift(LocalDateTime startTime, LocalDateTime endTime, Hall hall, Staff staff) {
        FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
        FieldValidator.validateDateTimeNotInThePast(endTime, "End Time");
        FieldValidator.validateStartTimeIsAfterEndTime(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
        id = UUID.randomUUID();

        assignHall(hall);
        assignStaff(staff);
    }

    private Shift() {
    }

    public void assignHall(Hall hall){
        FieldValidator.validateObjectNotNull(hall, "hall");

        if(this.hall != null && !this.hall.equals(hall)){
            this.hall.removeShift(this);
        }

        this.hall = hall;
        hall.addShift(this);
    }

    public void assignStaff(Staff staff){
        FieldValidator.validateObjectNotNull(staff, "staff");

        if(this.staff != null && !this.staff.equals(staff)){
            this.staff.removeShift(this);
        }

        this.staff = staff;
        staff.addShift(this);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
        FieldValidator.validateStartTimeIsAfterEndTime(startTime, endTime);
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        FieldValidator.validateDateTimeNotInThePast(endTime, "End Time");
        FieldValidator.validateStartTimeIsAfterEndTime(startTime, endTime);
        this.endTime = endTime;
    }

    public UUID getId() {
        return id;
    }

    public Hall getHall() {
        return hall;
    }

    public Staff getStaff() {
        return staff;
    }
}
