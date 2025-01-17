package com.chumakoff.mealvoting.config.security;

import com.chumakoff.mealvoting.model.User;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.Collections;

@Getter
public class AuthUser extends org.springframework.security.core.userdetails.User {
    private final Long userId;

    public AuthUser(@NonNull User user) {
        super(user.getLogin(), user.getPassword(), Collections.singletonList(user.getRole()));
        this.userId = user.getId();
    }
}
