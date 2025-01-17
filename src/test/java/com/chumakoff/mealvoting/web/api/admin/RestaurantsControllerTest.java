package com.chumakoff.mealvoting.web.api.admin;

import com.chumakoff.mealvoting.dto.RestaurantCreateDTO;
import com.chumakoff.mealvoting.dto.RestaurantResponseDTO;
import com.chumakoff.mealvoting.dto.RestaurantUpdateDTO;
import com.chumakoff.mealvoting.repository.RestaurantRepository;
import com.chumakoff.mealvoting.test_support.web.api.ApiControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.chumakoff.mealvoting.test_support.web.api.TestDBData.AUTH_ADMIN_LOGIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantsControllerTest extends ApiControllerTest {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void list() throws Exception {
        ResultActions response = performGetRequest("/api/admin/restaurants")
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<RestaurantResponseDTO> responseRestaurants = parseJsonResponseAsList(response, RestaurantResponseDTO.class);
        assertFalse(responseRestaurants.isEmpty());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void create() throws Exception {
        RestaurantCreateDTO createDto = new RestaurantCreateDTO("Restaurant Name");
        ResultActions response = perform(postRequest("/api/admin/restaurants").content(buildJSON(createDto)))
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

        ResultActions response = perform(patchRequest("/api/admin/restaurants/" + restaurantId).content(buildJSON(updateDto)))
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

        perform(deleteRequest("/api/admin/restaurants/" + restaurantId)).andExpect(status().isNoContent());
        assertFalse(restaurantRepository.existsById(restaurantId));
    }
}