package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.AgeRestrictionEnum;
import com.magicscreencinema.domain.validation.FieldValidator;

import java.time.LocalDate;

public class Movie {
    public String name;
    public AgeRestrictionEnum ageRestriction;
    public String description;
    public LocalDate premiereDate;
    public long movieDuration;

    public Movie(String name, AgeRestrictionEnum ageRestriction, String description, LocalDate premiereDate, long movieDuration) {
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
        this.ageRestriction = FieldValidator.validateObjectNotNull(ageRestriction, "Age Restriction");
        this.description = FieldValidator.validateNullOrEmptyString(description, "Description");
        this.premiereDate = FieldValidator.validateDateNotInThePast(premiereDate, "Premiere Date");
        this.movieDuration = FieldValidator.validatePositiveNumber(movieDuration, "Movie Duration");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
    }

    public AgeRestrictionEnum getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(AgeRestrictionEnum ageRestriction) {
        this.ageRestriction = FieldValidator.validateObjectNotNull(ageRestriction, "Age Restriction");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = FieldValidator.validateNullOrEmptyString(description, "Description");
    }

    public LocalDate getPremiereDate() {
        return premiereDate;
    }

    public void setPremiereDate(LocalDate premiereDate) {
        this.premiereDate = FieldValidator.validateDateNotInThePast(premiereDate, "Premiere Date");
    }

    public long getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(long movieDuration) {
        this.movieDuration = FieldValidator.validatePositiveNumber(movieDuration, "Movie Duration");
    }


}