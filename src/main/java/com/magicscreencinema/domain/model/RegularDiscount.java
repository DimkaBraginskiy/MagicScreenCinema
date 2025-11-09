package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;

import java.time.DayOfWeek;
import java.util.List;

public class RegularDiscount {
    private List<DayOfWeek> dayOfWeek;

    public RegularDiscount(List<DayOfWeek> dayOfWeek) {
        this.dayOfWeek = FieldValidator.validateDayOfWeekList(dayOfWeek, "Day Of Week List");
    }

    public void setDayOfWeek(List<DayOfWeek> dayOfWeek) {
        this.dayOfWeek = FieldValidator.validateDayOfWeekList(dayOfWeek, "Day Of Week List");
    }

    public List<DayOfWeek> getDayOfWeek() {
        return dayOfWeek;
    }
}
