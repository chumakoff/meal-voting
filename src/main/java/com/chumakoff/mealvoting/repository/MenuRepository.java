package com.chumakoff.mealvoting.repository;

import com.chumakoff.mealvoting.model.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Override
    @EntityGraph("Menu.withRestaurant")
    Optional<Menu> findById(Long id);

    @Override
    @EntityGraph("Menu.withRestaurant")
    List<Menu> findAll(Sort sort);

    @EntityGraph("Menu.withRestaurant")
    List<Menu> findAllByDate(LocalDate date, Sort sort);
}
