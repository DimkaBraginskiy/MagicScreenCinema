package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.AgeGroupEnum;
import com.magicscreencinema.domain.validation.FieldValidator;

public class AgeGroupDiscount {
    private String description;
    private AgeGroupEnum group;

    public AgeGroupDiscount(String description, AgeGroupEnum group) {
        this.description = FieldValidator.validateNullOrEmptyString(description, "Description");
        this.group = FieldValidator.validateObjectNotNull(group, "Group");
    }

    public void setDescription(String description) {
        this.description = FieldValidator.validateNullOrEmptyString(description, "Description");
    }

    public void setGroup(AgeGroupEnum group) {
        this.group = FieldValidator.validateObjectNotNull(group, "Group");
    }

    public String getDescription() {
        return description;
    }

    public AgeGroupEnum getGroup() {
        return group;
    }
}
