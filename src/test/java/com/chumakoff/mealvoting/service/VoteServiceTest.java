package com.chumakoff.mealvoting.service;

import com.chumakoff.mealvoting.config.exception.ApiErrorException;
import com.chumakoff.mealvoting.model.Vote;
import com.chumakoff.mealvoting.repository.RestaurantRepository;
import com.chumakoff.mealvoting.repository.UserRepository;
import com.chumakoff.mealvoting.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import static com.chumakoff.mealvoting.service.VoteService.VOTING_END_TIME;
import static com.chumakoff.mealvoting.testsupport.web.api.TestDBData.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VoteServiceTest {
    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    LocalDateTime votingEndTime;
    LocalDate currentDate;

    @BeforeEach
    public void setUp() {
        votingEndTime = LocalDateTime.of(LocalDate.now(), VOTING_END_TIME);
        currentDate = votingEndTime.toLocalDate();
    }

    @Test
    void registerVote__beforeVotingTimeEnds() {
        LocalDateTime currentTime = votingEndTime.minusSeconds(60);

        assertNewVoteSuccessfullyCreated(
                () -> voteService.registerVote(AUTH_USER_ID, TEST_RESTAURANT_ID, currentTime)
        );
    }

    @Test
    void registerVote__beforeVotingTimeEnds__whenVoteExists() {
        LocalDateTime currentTime = votingEndTime.minusSeconds(60);
        createUserVote(currentDate);

        Vote existingVoteBefore = findUserVote(currentDate).orElseThrow();
        Long restaurantIdBefore = existingVoteBefore.getRestaurant().getId();

        Vote resultVote = voteService.registerVote(AUTH_USER_ID, TEST_RESTAURANT_2_ID, currentTime);

        Vote existingVoteAfter = findUserVote(currentDate).orElseThrow();
        assertEquals(resultVote, existingVoteAfter);
        assertEquals(existingVoteBefore.getId(), existingVoteAfter.getId());
        assertNotEquals(restaurantIdBefore, existingVoteAfter.getRestaurant().getId());
        assertEquals(TEST_RESTAURANT_2_ID, existingVoteAfter.getRestaurant().getId());
    }

    @Test
    void registerVote__afterVotingTimeEnds() {
        LocalDateTime currentTime = votingEndTime.plusSeconds(60);

        assertNewVoteSuccessfullyCreated(
                () -> voteService.registerVote(AUTH_USER_ID, TEST_RESTAURANT_ID, currentTime)
        );
    }

    @Test
    void registerVote__afterVotingTimeEnds__whenVoteExists() {
        LocalDateTime currentTime = votingEndTime.plusSeconds(60);
        createUserVote(currentDate);

        ApiErrorException exception = assertThrows(ApiErrorException.class, () -> {
            voteService.registerVote(AUTH_USER_ID, TEST_RESTAURANT_ID, currentTime);
        });

        assertEquals("It is too late, vote can't be changed", exception.getMessage());
    }

    private Vote assertUserVoteExists(LocalDate date) {
        Optional<Vote> optional = findUserVote(date);
        assertTrue(optional.isPresent());
        return optional.get();
    }

    private void assertUserVoteDoesNotExist(LocalDate date) {
        assertFalse(findUserVote(date).isPresent());
    }

    private Optional<Vote> findUserVote(LocalDate date) {
        return voteRepository.findByUserIdAndMealDate(AUTH_USER_ID, date);
    }

    private void createUserVote(LocalDate date) {
        voteRepository.save(
                new Vote(
                        date,
                        userRepository.getReferenceById(AUTH_USER_ID),
                        restaurantRepository.getReferenceById(TEST_RESTAURANT_ID)
                )
        );
    }

    private void assertNewVoteSuccessfullyCreated(Supplier<Vote> action) {
        assertUserVoteDoesNotExist(currentDate);
        Vote resultVote = action.get();
        Vote dbVote = assertUserVoteExists(currentDate);
        assertEquals(resultVote, dbVote);
    }
}