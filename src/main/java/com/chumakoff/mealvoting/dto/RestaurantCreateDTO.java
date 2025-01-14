package com.chumakoff.mealvoting.dto;

import jakarta.validation.constraints.NotBlank;

public record RestaurantCreateDTO(@NotBlank String name) {
}
