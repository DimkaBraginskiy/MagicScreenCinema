package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.InvalidDateTimeRangeException;
import com.magicscreencinema.domain.validation.FieldValidator;

import java.time.LocalDateTime;

public class Shift {
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Shift(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) throw new InvalidDateTimeRangeException("StartTime can not be bigger than EndTime");

        this.startTime = FieldValidator.validateDateTimeNotInThePast(startTime, "Start time");
        this.endTime = FieldValidator.validateDateTimeNotInThePast(endTime, "End Time");
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = FieldValidator.validateDateTimeNotInThePast(startTime, "Start time");
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = FieldValidator.validateDateTimeNotInThePast(endTime, "End Time");
    }
}
