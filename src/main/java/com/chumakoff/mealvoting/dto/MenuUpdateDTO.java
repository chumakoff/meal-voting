package com.chumakoff.mealvoting.dto;

import com.chumakoff.mealvoting.model.Dish;
import com.chumakoff.mealvoting.validation.NullOrNotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record MenuUpdateDTO(
        @JsonProperty("menu_date") LocalDate menuDate,
        @NullOrNotEmpty List<Dish> dishes
) {
}