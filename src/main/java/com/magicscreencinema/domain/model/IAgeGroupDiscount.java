package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.AgeGroupEnum;

import java.util.UUID;

public interface IAgeGroupDiscount {
    void setDescription(String description);

    void setGroup(AgeGroupEnum group);

    String getDescription();

    AgeGroupEnum getGroup();

    UUID getId();
}
