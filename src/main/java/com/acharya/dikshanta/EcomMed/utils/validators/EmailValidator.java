package com.acharya.dikshanta.EcomMed.utils.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidatorConstraint.class)
public @interface EmailValidator {
    String message() default "Please provide valid email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
