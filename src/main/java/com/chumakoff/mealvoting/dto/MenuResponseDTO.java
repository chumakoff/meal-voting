package com.chumakoff.mealvoting.dto;

import com.chumakoff.mealvoting.model.Dish;
import com.chumakoff.mealvoting.model.Menu;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record MenuResponseDTO(
        Long id,
        @JsonProperty("menu_date") LocalDate menuDate,
        List<Dish> dishes,
        @JsonProperty("restaurant_id") Long restaurantId
) {
    public static MenuResponseDTO buildFromEntity(Menu menu) {
        return new MenuResponseDTO(
                menu.getId(),
                menu.getMenuDate(),
                menu.getDishes(),
                menu.getRestaurant().getId()
        );
    }
}
