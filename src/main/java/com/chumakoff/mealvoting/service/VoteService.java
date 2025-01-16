package com.chumakoff.mealvoting.service;

import com.chumakoff.mealvoting.model.Restaurant;
import com.chumakoff.mealvoting.model.User;
import com.chumakoff.mealvoting.model.Vote;
import com.chumakoff.mealvoting.repository.RestaurantRepository;
import com.chumakoff.mealvoting.repository.UserRepository;
import com.chumakoff.mealvoting.repository.VoteRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class VoteService {
    final private VoteRepository voteRepository;
    final private RestaurantRepository restaurantRepository;
    final private UserRepository userRepository;

    public VoteService(VoteRepository voteRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public List<Vote> findAll(LocalDate mealDate, Long userId, Sort sort) {
        if (mealDate == null && userId == null) {
            return voteRepository.findAll(sort);
        } else if (userId == null) {
            return voteRepository.findAllByMealDate(mealDate, sort);
        } else if (mealDate == null) {
            return voteRepository.findAllByUserId(userId, sort);
        } else {
            return voteRepository.findAllByMealDateAndUserId(mealDate, userId, sort);
        }
    }

    public Vote registerVote(Long userId, Long restaurantId, Instant currentTime) {
        // TODO change exception
        var user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var currentDate = LocalDate.ofInstant(currentTime, ZoneId.systemDefault());
        Optional<Vote> existingVote = voteRepository.findByUserIdAndMealDate(user.getId(), currentDate);

        if (existingVote.isPresent() && !canRevote(currentTime)) {
            throw new RuntimeException("It is too late, vote can't be changed");
        }

        // TODO change exception
        var restaurant = restaurantRepository.findById(restaurantId).orElseThrow();

        return existingVote.map(vote -> updateVote(vote, restaurant))
                .orElseGet(() -> createVote(user, restaurant, currentDate));
    }

    private Vote createVote(User user, Restaurant restaurant, LocalDate mealDate) {
        Vote vote = new Vote(mealDate, user, restaurant);
        return voteRepository.save(vote);
    }

    private Vote updateVote(Vote vote, Restaurant restaurant) {
        if (vote.getRestaurant().equals(restaurant)) {
            return vote;
        }

        vote.setRestaurant(restaurant);
        return voteRepository.save(vote);
    }

    private boolean canRevote(Instant time) {
        var currentLocalTime = LocalTime.ofInstant(time, ZoneId.systemDefault());
        return currentLocalTime.isBefore(LocalTime.of(11, 0));
    }
}
