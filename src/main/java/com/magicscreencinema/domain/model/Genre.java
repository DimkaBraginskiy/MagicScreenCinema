package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.util.UUID;

@ElementCollection(name = "genres")
public class Genre {
    @Id
    private UUID id;
    private String name;

    public Genre(String name) {
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
    }

    public Genre() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
    }
}
