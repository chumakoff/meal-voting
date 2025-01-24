package com.chumakoff.mealvoting.dto;

import com.chumakoff.mealvoting.model.Dish;
import com.chumakoff.mealvoting.model.Menu;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record MenuWithRestaurantResponseDTO(
        Long id,
        @JsonProperty("menu_date") LocalDate menuDate,
        List<Dish> dishes,
        RestaurantResponseDTO restaurant
) {
    public static MenuWithRestaurantResponseDTO buildFromEntity(Menu menu) {
        return new MenuWithRestaurantResponseDTO(
                menu.getId(),
                menu.getMenuDate(),
                menu.getDishes(),
                RestaurantResponseDTO.buildFromEntity(menu.getRestaurant())
        );
    }
}
