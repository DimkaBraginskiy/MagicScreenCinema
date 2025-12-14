package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.AgeGroupEnum;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@ElementCollection(name = "regularDiscounts")
public class RegularDiscount extends Discount{
    private List<DayOfWeek> dayOfWeek;

    public RegularDiscount(List<DayOfWeek> dayOfWeek, double discountAmount, String promoCode, String specialConditionDescription) {
        super(discountAmount, promoCode, specialConditionDescription);
        this.dayOfWeek = FieldValidator.validateDayOfWeekList(dayOfWeek, "Day Of Week List");
    }

    public RegularDiscount(List<DayOfWeek> dayOfWeek, double discountAmount, String promoCode, String ageGroupDiscountDescription, AgeGroupEnum ageGroup) {
        super(discountAmount, promoCode, ageGroupDiscountDescription, ageGroup);
        this.dayOfWeek = FieldValidator.validateDayOfWeekList(dayOfWeek, "Day Of Week List");
    }

    private RegularDiscount() {
    }

    public void setDayOfWeek(List<DayOfWeek> dayOfWeek) {
        this.dayOfWeek = FieldValidator.validateDayOfWeekList(dayOfWeek, "Day Of Week List");
    }

    public List<DayOfWeek> getDayOfWeek() {
        return dayOfWeek;
    }
}
