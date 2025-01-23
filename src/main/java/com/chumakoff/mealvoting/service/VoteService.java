package com.chumakoff.mealvoting.service;

import com.chumakoff.mealvoting.config.exception.ApiErrorException;
import com.chumakoff.mealvoting.model.Restaurant;
import com.chumakoff.mealvoting.model.User;
import com.chumakoff.mealvoting.model.Vote;
import com.chumakoff.mealvoting.repository.RestaurantRepository;
import com.chumakoff.mealvoting.repository.UserRepository;
import com.chumakoff.mealvoting.repository.VoteRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.chumakoff.mealvoting.helper.RepositoryHelper.getOrThrow;

@Service
public class VoteService {
    public final static LocalTime VOTING_END_TIME = LocalTime.of(11, 0);

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

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
            return voteRepository.findAllByUserIdAndMealDate(userId, mealDate, sort);
        }
    }

    @Transactional
    public Vote registerVote(Long userId, Long restaurantId, LocalDateTime currentTime) {
        User user = userRepository.getReferenceById(userId);
        var currentDate = LocalDate.from(currentTime);
        Optional<Vote> existingVote = voteRepository.findByUserIdAndMealDate(user.getId(), currentDate);

        if (existingVote.isPresent() && !canRevote(currentTime)) {
            throw new ApiErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "It is too late, vote can't be changed");
        }

        var restaurant = getOrThrow(restaurantRepository.findById(restaurantId), restaurantId, Restaurant.class);

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

    private boolean canRevote(LocalDateTime currentTime) {
        return currentTime.toLocalTime().isBefore(VOTING_END_TIME);
    }
}
