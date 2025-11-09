package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.DayOfWeekEnum;
import com.magicscreencinema.domain.validation.FieldValidator;

import java.util.List;
import java.util.Objects;

public class Regular {
    private List<DayOfWeekEnum> dayOfWeek;

    public Regular(List<DayOfWeekEnum> dayOfWeek) {
        this.dayOfWeek = FieldValidator.validateDayOfWeekList(dayOfWeek, "Day Of Week List");
    }

    public void setDayOfWeek(List<DayOfWeekEnum> dayOfWeek) {
        this.dayOfWeek = FieldValidator.validateDayOfWeekList(dayOfWeek, "Day Of Week List");
    }

    public List<DayOfWeekEnum> getDayOfWeek() {
        return dayOfWeek;
    }
}
