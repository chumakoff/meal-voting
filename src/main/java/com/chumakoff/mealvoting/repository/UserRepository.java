package com.chumakoff.mealvoting.repository;

import com.chumakoff.mealvoting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginIgnoreCase(String login);
}
