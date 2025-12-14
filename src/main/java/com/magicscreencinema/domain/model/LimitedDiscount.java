package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.AgeGroupEnum;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@ElementCollection(name = "limitedDiscounts")
public class LimitedDiscount extends Discount{
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public LimitedDiscount(LocalDateTime startTime, LocalDateTime endTime, double discountAmount, String promoCode, String ageGroupDiscountDescription, AgeGroupEnum ageGroup) {
        super(discountAmount, promoCode, ageGroupDiscountDescription, ageGroup);
        FieldValidator.validateDateTimeNotInThePast(endTime, "End Time");
        FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
        FieldValidator.validateStartTimeIsAfterEndTime(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LimitedDiscount(LocalDateTime startTime, LocalDateTime endTime, double discountAmount, String promoCode, String specialConditionDescription) {
        super(discountAmount, promoCode, specialConditionDescription);
        FieldValidator.validateDateTimeNotInThePast(endTime, "End Time");
        FieldValidator.validateDateTimeNotInThePast(startTime, "Start Time");
        FieldValidator.validateStartTimeIsAfterEndTime(startTime, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
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
}
