package com.chumakoff.mealvoting.dto;

import com.chumakoff.mealvoting.model.Restaurant;

public record RestaurantResponseDTO(Long id, String name) {
    public static RestaurantResponseDTO buildFromEntity(Restaurant restaurant) {
        return new RestaurantResponseDTO(restaurant.getId(), restaurant.getName());
    }
}
