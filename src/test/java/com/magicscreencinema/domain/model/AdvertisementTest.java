package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.exception.EmptyStringException;
import com.magicscreencinema.domain.exception.NonPositiveValueException;
import com.magicscreencinema.domain.exception.NullAttributeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class AdvertisementTest {
    @Test
    public void constructor_WithValidParameters_ShouldCreateAdvertisement() {
        Advertisement ad = new Advertisement("Summer Sale", 30, "CompanyX");
        assertEquals("Summer Sale", ad.getName());
        assertEquals(30, ad.getDuration());
        assertEquals("CompanyX", ad.getAdvertiserName());
    }

    @Test
    public void constructor_WithNullNameField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Advertisement(null, 30, "CompanyX");
        });
        assertEquals("Name can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyNameField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new Advertisement("", 30, "CompanyX");
        });
        assertEquals("Name can not be empty", exception.getMessage());
    }

    @Test
    public void constructor_WithNullAdvertiserNameField_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            new Advertisement("Summer sale", 30, null);
        });
        assertEquals("Advertiser Name can not be null", exception.getMessage());
    }

    @Test
    public void constructor_WithEmptyAdvertiserNameField_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            new Advertisement("Summer sale", 30, "");
        });
        assertEquals("Advertiser Name can not be empty", exception.getMessage());
    }

    @Test
    public void constructor_WithNegativeValueDurationField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            new Advertisement("Summer sale", -20, "CompanyX");
        });
        assertEquals("Duration must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void constructor_WithZeroValueDurationField_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            new Advertisement("Summer sale", 0, "CompanyX");
        });
        assertEquals("Duration must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setName_WithNullNameParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Advertisement ad = new Advertisement("Summer sale", 30, "CompanyX");
            ad.setName(null);
        });
        assertEquals("Name can not be null", exception.getMessage());
    }

    @Test
    public void setName_WithEmptyNameParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            Advertisement ad = new Advertisement("Summer sale", 30, "CompanyX");
            ad.setName("");
        });
        assertEquals("Name can not be empty", exception.getMessage());
    }

    @Test
    public void setName_WithValidParameter_ShouldChangeName() {
        Advertisement ad = new Advertisement("Summer sale", 30, "CompanyX");
        ad.setName("Winter sale");

        assertEquals("Winter sale", ad.getName());
    }

    @Test
    public void setAdvertiserName_WithNullAdvertiserNameParameter_ShouldThrowNullAttributeException() {
        NullAttributeException exception = assertThrows(NullAttributeException.class, () -> {
            Advertisement ad = new Advertisement("Summer sale", 30, "CompanyX");
            ad.setAdvertiserName(null);
        });
        assertEquals("Advertiser Name can not be null", exception.getMessage());
    }

    @Test
    public void setAdvertiserName_WithEmptyAdvertiserNameParameter_ShouldThrowEmptyStringException() {
        EmptyStringException exception = assertThrows(EmptyStringException.class, () -> {
            Advertisement ad = new Advertisement("Summer sale", 30, "CompanyX");
            ad.setAdvertiserName("");
        });
        assertEquals("Advertiser Name can not be empty", exception.getMessage());
    }

    @Test
    public void setAdvertiserName_WithValidParameter_ShouldChangeAdvertiserName() {
        Advertisement ad = new Advertisement("Summer sale", 30, "CompanyX");
        ad.setAdvertiserName("CompanyY");

        assertEquals("CompanyY", ad.getAdvertiserName());
    }

    @Test
    public void setDuration_WithNegativeDurationParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Advertisement ad = new Advertisement("Summer sale", 30, "CompanyX");
            ad.setDuration(-2);
        });
        assertEquals("Duration must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setDuration_WithZeroDurationParameter_ShouldThrowNonPositiveValueException() {
        NonPositiveValueException exception = assertThrows(NonPositiveValueException.class, () -> {
            Advertisement ad = new Advertisement("Summer sale", 30, "CompanyX");
            ad.setDuration(0);
        });
        assertEquals("Duration must be a positive value ( > 0).", exception.getMessage());
    }

    @Test
    public void setDuration_WithValidParameter_ShouldChangeDurationValue() {
        Advertisement ad = new Advertisement("Summer sale", 30, "CompanyX");
        ad.setDuration(2);

        assertEquals(2, ad.getDuration());
    }
}
