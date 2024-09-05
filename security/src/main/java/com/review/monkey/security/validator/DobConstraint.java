package com.review.monkey.security.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.lang.reflect.Field;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
    String message () default "Invalid date of birth";

    int min ();
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
