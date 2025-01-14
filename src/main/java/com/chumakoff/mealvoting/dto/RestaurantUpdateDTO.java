package com.chumakoff.mealvoting.dto;

import jakarta.validation.constraints.NotBlank;

public record RestaurantUpdateDTO(@NotBlank String name) {
}
