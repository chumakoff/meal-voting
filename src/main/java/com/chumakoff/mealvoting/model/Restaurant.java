package com.chumakoff.mealvoting.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Restaurant extends BaseEntity {
    @NotBlank
    private String name;

    public Restaurant(@NotNull String name) {
        super();
        this.name = name;
    }
}
