package com.chumakoff.mealvoting.dto;

import com.chumakoff.mealvoting.model.Dish;
import com.chumakoff.mealvoting.validation.NullOrNotEmpty;

import java.time.LocalDate;
import java.util.List;

public record MenuUpdateDTO(
        LocalDate menuDate,
        @NullOrNotEmpty List<Dish> dishes
) {
}