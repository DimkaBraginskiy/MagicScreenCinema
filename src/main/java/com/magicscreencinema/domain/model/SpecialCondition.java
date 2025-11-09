package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;

public class SpecialCondition {
    private String conditionDescription;

    public SpecialCondition(String conditionDescription) {
        this.conditionDescription = FieldValidator.validateNullOrEmptyString(conditionDescription, "Condition Description");
    }

    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = FieldValidator.validateNullOrEmptyString(conditionDescription, "Condition Description");
    }

    public String getConditionDescription() {
        return conditionDescription;
    }
}
