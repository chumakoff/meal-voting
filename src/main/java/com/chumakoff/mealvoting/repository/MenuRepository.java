package com.chumakoff.mealvoting.repository;

import com.chumakoff.mealvoting.model.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    // TODO
    // @Query("SELECT m from Menu m LEFT JOIN FETCH m.restaurant")
    // List<Menu> findAllFetchRestaurant(Sort id);

    List<Menu> findAllByDate(LocalDate date, Sort sort);
}
