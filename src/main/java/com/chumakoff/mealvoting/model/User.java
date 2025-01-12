package com.chumakoff.mealvoting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {
    @NonNull
    private String login;

    @Enumerated(EnumType.STRING)
    @NonNull
    private Role role;

    @NonNull
    private String password;
}
