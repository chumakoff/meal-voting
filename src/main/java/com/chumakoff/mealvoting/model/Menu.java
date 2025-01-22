package com.chumakoff.mealvoting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@NamedEntityGraph(name = "Menu.withRestaurant", attributeNodes = @NamedAttributeNode("restaurant"))
public class Menu extends BaseEntity {
    @NotNull
    private LocalDate menuDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    @NotEmpty
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Dish> dishes;

    public Menu(@NotNull LocalDate menuDate, @NotNull Restaurant restaurant, @NotEmpty List<Dish> dishes) {
        super();
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.dishes = dishes;
    }
}
