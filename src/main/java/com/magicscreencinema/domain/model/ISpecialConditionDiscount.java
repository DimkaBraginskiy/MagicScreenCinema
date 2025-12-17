package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;

import java.util.UUID;

public interface ISpecialConditionDiscount {
    void setConditionDescription(String conditionDescription);

    String getConditionDescription();

    UUID getId();
}
