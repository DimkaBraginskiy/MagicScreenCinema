package com.magicscreencinema.persistence.declaration;

import com.magicscreencinema.persistence.QualifierKeyConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Qualifier {
    Class<? extends QualifierKeyConverter<?>> converter();
    String referenceCollectionName();
}
