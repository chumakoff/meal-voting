package com.chumakoff.mealvoting.dto;

import com.chumakoff.mealvoting.model.Menu;
import com.chumakoff.mealvoting.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class MenuValidateDTO {
    private Long id;
    @NonNull private Restaurant restaurant;
    @NonNull private LocalDate menuDate;

    public static MenuValidateDTO buildFromEntity(Menu menu) {
       return new MenuValidateDTO(menu.getId(), menu.getRestaurant(), menu.getMenuDate());
    }
}
