package com.chumakoff.mealvoting.model;

import com.chumakoff.mealvoting.utils.MenuDishesAttributeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@NamedEntityGraph(name = "Menu.withRestaurant", attributeNodes = @NamedAttributeNode("restaurant"))
public class Menu extends BaseEntity {
    @NotNull
    private LocalDate date;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @NotEmpty
    @Convert(converter = MenuDishesAttributeConverter.class)
    private List<Dish> dishes;

    public Menu(@NotNull LocalDate date, @NotNull Restaurant restaurant, @NotEmpty List<Dish> dishes) {
        super();
        this.date = date;
        this.restaurant = restaurant;
        this.dishes = dishes;
    }
}
