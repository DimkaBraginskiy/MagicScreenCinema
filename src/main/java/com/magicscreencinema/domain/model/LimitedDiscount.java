package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@ElementCollection(name = "limitedDiscounts")
public class LimitedDiscount {
    @Id
    private UUID id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public LimitedDiscount(LocalDateTime startTime, LocalDateTime endTime) {
        FieldValidator.validateDateTimeNotInThePast(endTime, "End Time");
        FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
        FieldValidator.validateStartTimeIsAfterEndTime(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
        id = UUID.randomUUID();
    }

    private LimitedDiscount() {
    }

    public void setStartTime(LocalDateTime startTime) {
        FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
        FieldValidator.validateStartTimeIsAfterEndTime(startTime, endTime);
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        FieldValidator.validateDateTimeNotInThePast(endTime, "End Time");
        FieldValidator.validateStartTimeIsAfterEndTime(startTime, endTime);
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public UUID getId() {
        return id;
    }
}
