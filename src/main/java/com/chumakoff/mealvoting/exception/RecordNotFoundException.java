package com.chumakoff.mealvoting.exception;

import jakarta.persistence.EntityNotFoundException;

public class RecordNotFoundException extends EntityNotFoundException {
    public RecordNotFoundException(Long id, Class<?> clazz) {
        super("Could not find " + clazz.getSimpleName() + " with id=" + id);
    }
}
