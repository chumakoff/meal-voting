package com.chumakoff.mealvoting.web.api;

import com.chumakoff.mealvoting.dto.VoteCreateDTO;
import com.chumakoff.mealvoting.dto.VoteResponseDTO;
import com.chumakoff.mealvoting.model.User;
import com.chumakoff.mealvoting.model.Vote;
import com.chumakoff.mealvoting.repository.UserRepository;
import com.chumakoff.mealvoting.repository.VoteRepository;
import com.chumakoff.mealvoting.testsupport.web.api.ApiControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static com.chumakoff.mealvoting.testsupport.web.api.TestDBData.AUTH_USER_LOGIN;
import static com.chumakoff.mealvoting.testsupport.web.api.TestDBData.TEST_RESTAURANT_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VotesControllerTest extends ApiControllerTest {
    private static final String VOTES_API_ENDPOINT = "/api/votes";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void list() throws Exception {
        ResultActions response = performGetRequest(VOTES_API_ENDPOINT)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<VoteResponseDTO> responseVotes = parseJsonResponseAsList(response, VoteResponseDTO.class);
        assertFalse(responseVotes.isEmpty());

        List<VoteResponseDTO> allExistingVotes = voteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream().map(VoteResponseDTO::buildFromEntity).toList();
        assertIterableEquals(responseVotes, allExistingVotes);
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void list__filterByDate() throws Exception {
        LocalDate today = LocalDate.now();
        ResultActions response = performGetRequest(VOTES_API_ENDPOINT + "?date=" + today)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<VoteResponseDTO> responseVotes = parseJsonResponseAsList(response, VoteResponseDTO.class);
        assertFalse(responseVotes.isEmpty());

        List<Vote> allVotes = voteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<VoteResponseDTO> expectedVotes = allVotes.stream().filter(v -> v.getMealDate().equals(today))
                .map(VoteResponseDTO::buildFromEntity).toList();
        assertNotEquals(allVotes.size(), expectedVotes.size());
        assertIterableEquals(responseVotes, expectedVotes);
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void list__filterByUserId() throws Exception {
        Long userId = 2L;
        ResultActions response = performGetRequest(VOTES_API_ENDPOINT + "?user_id=" + userId)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<VoteResponseDTO> responseVotes = parseJsonResponseAsList(response, VoteResponseDTO.class);
        assertFalse(responseVotes.isEmpty());

        List<Vote> allVotes = voteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<VoteResponseDTO> expectedVotes = allVotes.stream().filter(v -> v.getUser().getId().equals(userId))
                .map(VoteResponseDTO::buildFromEntity).toList();
        assertNotEquals(allVotes.size(), expectedVotes.size());
        assertIterableEquals(responseVotes, expectedVotes);
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void list__filterByUserIdAndDate() throws Exception {
        Long userId = 2L;
        LocalDate today = LocalDate.now();

        ResultActions response = performGetRequest(VOTES_API_ENDPOINT + "?user_id=" + userId + "&date=" + today)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<VoteResponseDTO> responseVotes = parseJsonResponseAsList(response, VoteResponseDTO.class);
        assertFalse(responseVotes.isEmpty());

        List<Vote> allVotes = voteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<Vote> votesFilteredByUser = allVotes.stream().filter(v -> v.getUser().getId().equals(userId)).toList();
        List<VoteResponseDTO> expectedVotes = votesFilteredByUser.stream().filter(v -> v.getMealDate().equals(today))
                .map(VoteResponseDTO::buildFromEntity).toList();
        assertNotEquals(allVotes.size(), votesFilteredByUser.size());
        assertNotEquals(votesFilteredByUser.size(), expectedVotes.size());
        assertIterableEquals(responseVotes, expectedVotes);
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void create() throws Exception {
        VoteCreateDTO requestDto = new VoteCreateDTO(TEST_RESTAURANT_ID);

        ResultActions response = perform(postRequest(VOTES_API_ENDPOINT).content(buildJSON(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        VoteResponseDTO responseVote = parseJsonResponse(response, VoteResponseDTO.class);
        assertNotNull(responseVote.id());
        assertEquals(responseVote.mealDate(), LocalDate.now());
        assertEquals(responseVote.restaurantId(), requestDto.restaurantId());
        assertEquals(responseVote.userId(), userRepository.findByLoginIgnoreCase(AUTH_USER_LOGIN).map(User::getId).orElseThrow());
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void create__withNonexistentRestaurant() throws Exception {
        VoteCreateDTO requestDto = new VoteCreateDTO(99999L);
        perform(postRequest(VOTES_API_ENDPOINT).content(buildJSON(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Could not find Restaurant with id=99999"));
    }
}