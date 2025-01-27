package com.chumakoff.mealvoting.web.api;

import com.chumakoff.mealvoting.dto.MenuWithRestaurantResponseDTO;
import com.chumakoff.mealvoting.model.Menu;
import com.chumakoff.mealvoting.repository.MenuRepository;
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
import static com.chumakoff.mealvoting.web.api.MenusController.MENUS_API_ENDPOINT;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MenusControllerTest extends ApiControllerTest {
    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void list() throws Exception {
        ResultActions response = performGetRequest(MENUS_API_ENDPOINT)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<MenuWithRestaurantResponseDTO> responseMenus = parseJsonResponseAsList(response, MenuWithRestaurantResponseDTO.class);
        assertFalse(responseMenus.isEmpty());

        List<MenuWithRestaurantResponseDTO> allExistingMenus = menuRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream().map(MenuWithRestaurantResponseDTO::buildFromEntity).toList();
        assertIterableEquals(responseMenus, allExistingMenus);
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void list__filterByDate() throws Exception {
        LocalDate today = LocalDate.now();
        ResultActions response = performGetRequest(MENUS_API_ENDPOINT + "?date=" + today)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<MenuWithRestaurantResponseDTO> responseMenus = parseJsonResponseAsList(response, MenuWithRestaurantResponseDTO.class);
        assertFalse(responseMenus.isEmpty());

        List<Menu> allMenus = menuRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<MenuWithRestaurantResponseDTO> expectedMenus = allMenus.stream().filter(m -> m.getMenuDate().equals(today))
                .map(MenuWithRestaurantResponseDTO::buildFromEntity).toList();
        assertNotEquals(allMenus.size(), expectedMenus.size());
        assertIterableEquals(responseMenus, expectedMenus);
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void get() throws Exception {
        Long menuId = 1L;
        ResultActions response = performGetRequest(menuUrl(menuId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        MenuWithRestaurantResponseDTO responseMenu = parseJsonResponse(response, MenuWithRestaurantResponseDTO.class);
        Menu menu = menuRepository.findById(menuId).orElseThrow();
        assertEquals(responseMenu, MenuWithRestaurantResponseDTO.buildFromEntity(menu));
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void get__nonexistent() throws Exception {
        performGetRequest(menuUrl(99999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Could not find Menu with id=99999"));
    }

    private String menuUrl(Long id) {
        return MENUS_API_ENDPOINT + "/" + id;
    }
}


