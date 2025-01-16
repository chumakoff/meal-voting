package com.chumakoff.mealvoting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record VoteCreateDTO(@NotNull @JsonProperty("restaurant_id") Long restaurantId) {
}
