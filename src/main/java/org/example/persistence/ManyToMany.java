package org.example.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToMany {
    String mappedBy() default "";
    Cascade cascade() default Cascade.NONE;
    Fetch fetch() default Fetch.LAZY;
}
