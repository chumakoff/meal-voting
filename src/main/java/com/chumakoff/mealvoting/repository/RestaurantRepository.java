package com.chumakoff.mealvoting.repository;

import com.chumakoff.mealvoting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
