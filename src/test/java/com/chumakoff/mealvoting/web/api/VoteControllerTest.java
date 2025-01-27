package com.chumakoff.mealvoting.web.api;

import com.chumakoff.mealvoting.dto.VoteCreateDTO;
import com.chumakoff.mealvoting.dto.VoteResponseDTO;
import com.chumakoff.mealvoting.model.User;
import com.chumakoff.mealvoting.repository.UserRepository;
import com.chumakoff.mealvoting.testsupport.web.api.ApiControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static com.chumakoff.mealvoting.testsupport.web.api.TestDBData.AUTH_USER_LOGIN;
import static com.chumakoff.mealvoting.testsupport.web.api.TestDBData.TEST_RESTAURANT_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VoteControllerTest extends ApiControllerTest {
    private static final String VOTE_API_ENDPOINT = "/api/vote";

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void update() throws Exception {
        VoteCreateDTO requestDto = new VoteCreateDTO(TEST_RESTAURANT_ID);

        ResultActions response = perform(putRequest(VOTE_API_ENDPOINT).content(buildJSON(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        VoteResponseDTO responseVote = parseJsonResponse(response, VoteResponseDTO.class);
        assertNotNull(responseVote.id());
        assertEquals(responseVote.mealDate(), LocalDate.now());
        assertEquals(responseVote.restaurantId(), requestDto.restaurantId());
        assertEquals(responseVote.userId(), userRepository.findByLoginIgnoreCase(AUTH_USER_LOGIN).map(User::getId).orElseThrow());
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void update__withNonexistentRestaurant() throws Exception {
        VoteCreateDTO requestDto = new VoteCreateDTO(99999L);
        perform(putRequest(VOTE_API_ENDPOINT).content(buildJSON(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Could not find Restaurant with id=99999"));
    }
}