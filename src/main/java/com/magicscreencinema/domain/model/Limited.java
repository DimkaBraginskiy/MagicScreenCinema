package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.InvalidDateTimeRangeException;
import com.magicscreencinema.domain.validation.FieldValidator;

import java.time.LocalDateTime;

public class Limited {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Limited(LocalDateTime startTime, LocalDateTime endTime) {
        FieldValidator.validateStartTimeIsAfterEndTime(startTime,endTime);

        this.startTime = FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
        this.endTime = FieldValidator.validateDateTimeNotInThePast(endTime, "End Time");
    }

    public void setStartTime(LocalDateTime startTime) {
        FieldValidator.validateStartTimeIsAfterEndTime(startTime,endTime);
        this.startTime = FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
    }

    public void setEndTime(LocalDateTime endTime) {
        FieldValidator.validateStartTimeIsAfterEndTime(startTime,endTime);
        this.endTime = FieldValidator.validateDateTimeNotInThePast(endTime, "End Time");
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
