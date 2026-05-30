package com.acharya.dikshanta.EcomMed.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class EmailValidatorConstraint implements ConstraintValidator<EmailValidator, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.endsWith("@gmail.com");
    }
}
