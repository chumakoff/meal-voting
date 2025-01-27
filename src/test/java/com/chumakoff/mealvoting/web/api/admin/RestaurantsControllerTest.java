package com.chumakoff.mealvoting.web.api.admin;

import com.chumakoff.mealvoting.dto.RestaurantCreateDTO;
import com.chumakoff.mealvoting.dto.RestaurantResponseDTO;
import com.chumakoff.mealvoting.dto.RestaurantUpdateDTO;
import com.chumakoff.mealvoting.repository.RestaurantRepository;
import com.chumakoff.mealvoting.testsupport.web.api.ApiControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.chumakoff.mealvoting.testsupport.web.api.TestDBData.AUTH_ADMIN_LOGIN;
import static com.chumakoff.mealvoting.testsupport.web.api.TestDBData.AUTH_USER_LOGIN;
import static com.chumakoff.mealvoting.web.api.admin.RestaurantsController.RESTAURANTS_API_ENDPOINT;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantsControllerTest extends ApiControllerTest {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void list() throws Exception {
        ResultActions response = performGetRequest(RESTAURANTS_API_ENDPOINT)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<RestaurantResponseDTO> responseRestaurants = parseJsonResponseAsList(response, RestaurantResponseDTO.class);
        assertFalse(responseRestaurants.isEmpty());
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void list__unauthorized() throws Exception {
        performGetRequest(RESTAURANTS_API_ENDPOINT).andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void create() throws Exception {
        RestaurantCreateDTO createDto = new RestaurantCreateDTO("Restaurant Name");
        ResultActions response = perform(postRequest(RESTAURANTS_API_ENDPOINT).content(buildJSON(createDto)))
                .andExpect(status().isCreated());
        RestaurantResponseDTO responseRestaurant = parseJsonResponse(response, RestaurantResponseDTO.class);
        assertNotNull(responseRestaurant.id());
        assertEquals(createDto.name(), responseRestaurant.name());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void update() throws Exception {
        Long restaurantId = 1L;
        RestaurantUpdateDTO updateDto = new RestaurantUpdateDTO("Updated Restaurant Name");

        ResultActions response = perform(patchRequest(restaurantUrl(restaurantId)).content(buildJSON(updateDto)))
                .andExpect(status().isOk());
        RestaurantResponseDTO responseRestaurant = parseJsonResponse(response, RestaurantResponseDTO.class);
        assertEquals(restaurantId, responseRestaurant.id());
        assertEquals(updateDto.name(), responseRestaurant.name());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void delete() throws Exception {
        Long restaurantId = 1L;
        assertTrue(restaurantRepository.existsById(restaurantId));

        perform(deleteRequest(restaurantUrl(restaurantId))).andExpect(status().isNoContent());
        assertFalse(restaurantRepository.existsById(restaurantId));
    }

    private String restaurantUrl(Long id) {
        return RESTAURANTS_API_ENDPOINT + "/" + id;
    }
}