package com.chumakoff.mealvoting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedEntityGraph;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "Vote.withAllAssociations", includeAllAttributes = true)
public class Vote extends BaseEntity {
    @NotNull
    private LocalDate mealDate;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private Restaurant restaurant;
}
