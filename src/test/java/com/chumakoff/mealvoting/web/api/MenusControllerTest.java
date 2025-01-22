package com.chumakoff.mealvoting.web.api;

import com.chumakoff.mealvoting.dto.MenuResponseDTO;
import com.chumakoff.mealvoting.model.Menu;
import com.chumakoff.mealvoting.repository.MenuRepository;
import com.chumakoff.mealvoting.test_support.web.api.ApiControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static com.chumakoff.mealvoting.test_support.web.api.TestDBData.AUTH_USER_LOGIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MenusControllerTest extends ApiControllerTest {
    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void list() throws Exception {
        ResultActions response = performGetRequest("/api/menus")
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<MenuResponseDTO> responseMenus = parseJsonResponseAsList(response, MenuResponseDTO.class);
        assertFalse(responseMenus.isEmpty());

        List<MenuResponseDTO> allExistingMenus = menuRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream().map(MenuResponseDTO::buildFromEntity).toList();
        assertIterableEquals(responseMenus, allExistingMenus);
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void list__filterByDate() throws Exception {
        LocalDate today = LocalDate.now();
        ResultActions response = performGetRequest("/api/menus?date=" + today)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<MenuResponseDTO> responseMenus = parseJsonResponseAsList(response, MenuResponseDTO.class);
        assertFalse(responseMenus.isEmpty());

        List<Menu> allMenus = menuRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<MenuResponseDTO> expectedMenus = allMenus.stream().filter(m -> m.getMenuDate().equals(today))
                .map(MenuResponseDTO::buildFromEntity).toList();
        assertNotEquals(allMenus.size(), expectedMenus.size());
        assertIterableEquals(responseMenus, expectedMenus);
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void get() throws Exception {
        Long menuId = 1L;
        ResultActions response = performGetRequest("/api/menus/" + menuId)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        MenuResponseDTO responseMenu = parseJsonResponse(response, MenuResponseDTO.class);
        Menu menu = menuRepository.findById(menuId).orElseThrow();
        assertEquals(responseMenu, MenuResponseDTO.buildFromEntity(menu));
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void get__nonexistent() throws Exception {
        performGetRequest("/api/menus/99999")
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Could not find Menu with id=99999"));
    }
}


