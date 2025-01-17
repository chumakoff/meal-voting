package com.chumakoff.mealvoting.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class NullOrNotEmptyValidator implements ConstraintValidator<NullOrNotEmpty, List<?>> {
    public void initialize(NullOrNotEmpty parameters) {
    }

    public boolean isValid(List<?> value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || !value.isEmpty();
    }
}
