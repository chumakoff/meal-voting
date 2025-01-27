package com.chumakoff.mealvoting.web.api.admin;

import com.chumakoff.mealvoting.dto.VoteResponseDTO;
import com.chumakoff.mealvoting.model.Vote;
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

import static com.chumakoff.mealvoting.testsupport.web.api.TestDBData.AUTH_ADMIN_LOGIN;
import static com.chumakoff.mealvoting.testsupport.web.api.TestDBData.AUTH_USER_LOGIN;
import static com.chumakoff.mealvoting.web.api.admin.VotesController.VOTES_API_ENDPOINT;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VotesControllerTest extends ApiControllerTest {
    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
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
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
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
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
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
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
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
    void list__unauthorized() throws Exception {
        performGetRequest(VOTES_API_ENDPOINT).andExpect(status().isForbidden());
    }
}