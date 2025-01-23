package com.chumakoff.mealvoting.helper;

import com.chumakoff.mealvoting.exception.RecordNotFoundException;

import java.util.Optional;

public class RepositoryHelper {
    public static <T> T getOrThrow(Optional<T> optional, Long id, Class<T> clazz) {
        return optional.orElseThrow(() -> new RecordNotFoundException(id, clazz));
    }
}
