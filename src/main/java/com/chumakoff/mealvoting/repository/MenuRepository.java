package com.chumakoff.mealvoting.repository;

import com.chumakoff.mealvoting.model.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m FROM Menu m WHERE m.id=:id")
    @EntityGraph("Menu.withRestaurant")
    Optional<Menu> findByIdWithRestaurant(Long id);

    @Query("SELECT m FROM Menu m")
    @EntityGraph("Menu.withRestaurant")
    List<Menu> findAllWithRestaurant(Sort sort);

    @Query("SELECT m FROM Menu m WHERE m.menuDate=:date")
    @EntityGraph("Menu.withRestaurant")
    List<Menu> findAllByMenuDateWithRestaurant(LocalDate date, Sort sort);

    boolean existsByMenuDateAndRestaurantId(LocalDate date, Long restaurantId);
}
