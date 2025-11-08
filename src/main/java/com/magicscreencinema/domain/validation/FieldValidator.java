package com.magicscreencinema.domain.validation;

import com.magicscreencinema.domain.exception.DateInFutureException;
import com.magicscreencinema.domain.exception.EmptyStringException;
import com.magicscreencinema.domain.exception.NullAttributeException;

import java.time.LocalDate;

public final class FieldValidator {
    public static String validateNullOrEmpty(String value, String fieldName){
        if(value == null){
            throw new NullAttributeException(fieldName + "  can not be null");
        }
        if(value.trim().isEmpty()){
            throw new EmptyStringException(fieldName + " can not be empty");
        }
        return value.trim();
    }

    public static String validateNullable(String value, String fieldName){
        if(value != null && value.isEmpty()){
            throw new EmptyStringException(fieldName + " can not be empty");
        }
        return value;
    }

    public static LocalDate validateBirthDate(LocalDate value, String fieldName){
        if(value == null){
            throw new NullAttributeException(fieldName + " can not be null");
        }
        if(value.isAfter(LocalDate.now())){
            throw new DateInFutureException(fieldName + " can not be in the future");
        }
        return value;
    }
}
