package com.mprtcz.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SameAccountValidator.class)
public @interface NotSameAccount {
    String message() default "Accounts cannot be the same";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
