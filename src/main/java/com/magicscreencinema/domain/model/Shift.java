package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@ElementCollection(name = "shifts")
public class Shift {
    @Id
    private UUID id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Shift(LocalDateTime startTime, LocalDateTime endTime) {
        FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
        FieldValidator.validateDateTimeNotInThePast(endTime, "End Time");
        FieldValidator.validateStartTimeIsAfterEndTime(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
        id = UUID.randomUUID();
    }

    private Shift() {
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
}
