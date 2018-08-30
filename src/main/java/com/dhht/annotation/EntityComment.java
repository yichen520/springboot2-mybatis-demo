package com.dhht.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EntityComment {
    String value() default "";
    int type() default 0;
}
