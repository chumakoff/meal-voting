package com.chumakoff.mealvoting.dto;

import com.chumakoff.mealvoting.model.Dish;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record MenuCreateDTO(
        @NotNull @JsonProperty("restaurant_id") Long restaurantId,
        @NotNull LocalDate date,
        @NotEmpty List<Dish> dishes
) {
}