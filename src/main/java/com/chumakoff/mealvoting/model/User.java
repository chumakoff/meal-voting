package com.chumakoff.mealvoting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class User extends BaseEntity {
    @NonNull
    private String login;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NonNull
    private String password;
}
