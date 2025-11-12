package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.util.UUID;

@ElementCollection(name = "specialConditionDiscounts")
public class SpecialConditionDiscount {
    @Id
    private UUID id;
    private String conditionDescription;

    public SpecialConditionDiscount(String conditionDescription) {
        this.conditionDescription = FieldValidator.validateNullOrEmptyString(conditionDescription, "Condition Description");
    }

    private SpecialConditionDiscount() {
    }

    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = FieldValidator.validateNullOrEmptyString(conditionDescription, "Condition Description");
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

    public UUID getId() {
        return id;
    }
}
