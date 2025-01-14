package com.chumakoff.mealvoting.dto;

import com.chumakoff.mealvoting.model.Dish;
import com.chumakoff.mealvoting.model.Menu;

import java.time.LocalDate;
import java.util.List;

public record MenuResponseDTO(
        Long id,
        LocalDate date,
        List<Dish> dishes,
        RestaurantResponseDTO restaurant
) {
    public static MenuResponseDTO buildFromEntity(Menu menu) {
        return new MenuResponseDTO(
                menu.getId(),
                menu.getDate(),
                menu.getDishes(),
                RestaurantResponseDTO.buildFromEntity(menu.getRestaurant())
        );
    }
}
