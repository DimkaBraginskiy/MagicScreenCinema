package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;

public class Advertisement {
    public String name;
    public long duration;
    public String advertiserName;

    public Advertisement(String name, long duration, String advertiserName) {
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
        this.duration = FieldValidator.validatePositiveNumber(duration, "Duration");
        this.advertiserName = FieldValidator.validateNullOrEmptyString(advertiserName, "Advertiser Name");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = FieldValidator.validatePositiveNumber(duration, "Duration");
    }

    public String getAdvertiserName() {
        return advertiserName;
    }

    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = FieldValidator.validateNullOrEmptyString(advertiserName, "Advertiser Name");
    }
}
