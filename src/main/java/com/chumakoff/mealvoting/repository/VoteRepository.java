package com.chumakoff.mealvoting.repository;

import com.chumakoff.mealvoting.model.Vote;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    String FIND_BY_USER_ID_AND_MEAL_DATE_QUERY = "SELECT v FROM Vote v WHERE v.user.id = :userId AND v.mealDate = :mealDate";

    List<Vote> findAllByMealDate(LocalDate mealDate, Sort sort);

    @Query("SELECT v FROM Vote v WHERE v.user.id = :userId")
    List<Vote> findAllByUserId(Long userId, Sort sort);

    @Query(FIND_BY_USER_ID_AND_MEAL_DATE_QUERY)
    List<Vote> findAllByUserIdAndMealDate(Long userId, LocalDate mealDate, Sort sort);

    @Query(FIND_BY_USER_ID_AND_MEAL_DATE_QUERY)
    Optional<Vote> findByUserIdAndMealDate(Long userId, LocalDate mealDate);
}
