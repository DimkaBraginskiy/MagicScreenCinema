package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;

public class SpecialConditionDiscount {
    private String conditionDescription;

    public SpecialConditionDiscount(String conditionDescription) {
        this.conditionDescription = FieldValidator.validateNullOrEmptyString(conditionDescription, "Condition Description");
    }

    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = FieldValidator.validateNullOrEmptyString(conditionDescription, "Condition Description");
    }

    public String getConditionDescription() {
        return conditionDescription;
    }
}
