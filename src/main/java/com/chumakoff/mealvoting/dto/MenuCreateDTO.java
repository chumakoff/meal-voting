package com.chumakoff.mealvoting.dto;

import com.chumakoff.mealvoting.model.Dish;
import com.chumakoff.mealvoting.validation.UniqueDatePerRestaurant;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@UniqueDatePerRestaurant
public record MenuCreateDTO(
        @NotNull @JsonProperty("restaurant_id") Long restaurantId,
        @NotNull LocalDate date,
        @NotEmpty List<Dish> dishes
) {
}