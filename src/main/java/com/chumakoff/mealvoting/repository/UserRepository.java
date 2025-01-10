package com.chumakoff.mealvoting.repository;

import com.chumakoff.mealvoting.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByLoginIgnoreCase(String login);
}
