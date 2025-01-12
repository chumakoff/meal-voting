package com.chumakoff.mealvoting.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Restaurant extends BaseEntity {
    @NotBlank
    @NonNull
    private String name;
}
