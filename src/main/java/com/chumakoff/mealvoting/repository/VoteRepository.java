package com.chumakoff.mealvoting.repository;

import com.chumakoff.mealvoting.model.Vote;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findAllByMealDate(LocalDate mealDate, Sort sort);

    List<Vote> findAllByUserId(Long userId, Sort sort);

    List<Vote> findAllByMealDateAndUserId(LocalDate mealDate, Long userId, Sort sort);

    Optional<Vote> findByUserIdAndMealDate(Long userId, LocalDate mealDate);
}
