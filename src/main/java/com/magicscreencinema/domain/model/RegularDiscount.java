package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@ElementCollection(name = "regularDiscounts")
public class RegularDiscount {
    @Id
    private UUID id;
    private List<DayOfWeek> dayOfWeek;

    public RegularDiscount(List<DayOfWeek> dayOfWeek) {
        this.dayOfWeek = FieldValidator.validateDayOfWeekList(dayOfWeek, "Day Of Week List");
        id = UUID.randomUUID();
    }

    private RegularDiscount() {
    }

    public void setDayOfWeek(List<DayOfWeek> dayOfWeek) {
        this.dayOfWeek = FieldValidator.validateDayOfWeekList(dayOfWeek, "Day Of Week List");
    }

    public List<DayOfWeek> getDayOfWeek() {
        return dayOfWeek;
    }

    public UUID getId() {
        return id;
    }
}
