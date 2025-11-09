package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LimitedDiscount {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public LimitedDiscount(LocalDateTime startTime, LocalDateTime endTime) {
        FieldValidator.validateDateTimeNotInThePast(endTime, "End Time");
        FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
        FieldValidator.validateStartTimeIsAfterEndTime(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
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
}
