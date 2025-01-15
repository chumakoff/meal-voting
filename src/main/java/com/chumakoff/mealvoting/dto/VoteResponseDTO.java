package com.chumakoff.mealvoting.dto;

import com.chumakoff.mealvoting.model.Vote;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record VoteResponseDTO(
        Long id,
        @JsonProperty("meal_date") LocalDate mealDate,
        @JsonProperty("user_id") Long userId,
        @JsonProperty("restaurant_id") Long restaurantId
) {
    public static VoteResponseDTO buildFromEntity(Vote vote) {
        return new VoteResponseDTO(
                vote.getId(),
                vote.getMealDate(),
                vote.getUser().getId(),
                vote.getRestaurant().getId()
        );
    }
}
