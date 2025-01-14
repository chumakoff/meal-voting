package com.chumakoff.mealvoting.dto;

import com.chumakoff.mealvoting.model.Dish;
import com.chumakoff.mealvoting.model.Menu;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record MenuResponseDTO(
        Long id,
        @JsonProperty("restaurant_id") Long restaurantId,
        LocalDate date,
        List<Dish> dishes
) {
    public static MenuResponseDTO buildFromEntity(Menu menu) {
        return new MenuResponseDTO(menu.getId(), menu.getRestaurant().getId(), menu.getDate(), menu.getDishes());
    }
}
