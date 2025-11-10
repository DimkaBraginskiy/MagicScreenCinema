package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;

public class Genre {
    private String name;

    public Genre(String name) {
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
    }
}
