package com.chumakoff.mealvoting.web.api.admin;

import com.chumakoff.mealvoting.dto.MenuCreateDTO;
import com.chumakoff.mealvoting.dto.MenuResponseDTO;
import com.chumakoff.mealvoting.dto.MenuUpdateDTO;
import com.chumakoff.mealvoting.model.Dish;
import com.chumakoff.mealvoting.model.Menu;
import com.chumakoff.mealvoting.repository.MenuRepository;
import com.chumakoff.mealvoting.test_support.web.api.ApiControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static com.chumakoff.mealvoting.test_support.web.api.TestDBData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenusControllerTest extends ApiControllerTest {
    private final LocalDate today = LocalDate.now();
    private final MenuCreateDTO menuCreateDto =
            new MenuCreateDTO(TEST_RESTAURANT_ID, today, List.of(new Dish("Foo", 100)));
    private final MenuUpdateDTO menuUpdateDto =
            new MenuUpdateDTO(today.plusDays(99), List.of(new Dish("Updated", 321)));

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void create() throws Exception {
        ResultActions response = perform(postRequest("/api/admin/menus").content(buildJSON(menuCreateDto)))
                .andExpect(status().isCreated());
        MenuResponseDTO responseMenu = parseJsonResponse(response, MenuResponseDTO.class);
        assertNotNull(responseMenu.id());
        assertEquals(menuCreateDto.date(), responseMenu.date());
        assertEquals(menuCreateDto.dishes(), responseMenu.dishes());
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void create__unauthorized() throws Exception {
        perform(postRequest("/api/admin/menus").content(buildJSON(menuCreateDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void create__invalid() throws Exception {
        // TODO
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void update() throws Exception {
        Long menuId = 1L;
        ResultActions response = perform(patchRequest("/api/admin/menus/" + menuId).content(buildJSON(menuUpdateDto)))
                .andExpect(status().isOk());
        MenuResponseDTO responseMenu = parseJsonResponse(response, MenuResponseDTO.class);

        assertEquals(menuId, responseMenu.id());
        assertEquals(menuUpdateDto.date(), responseMenu.date());
        assertEquals(menuUpdateDto.dishes(), responseMenu.dishes());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void update__onlyDate() throws Exception {
        Long menuId = 1L;
        MenuUpdateDTO updateDto = new MenuUpdateDTO(menuUpdateDto.date(), null);
        Menu menuBeforeUpdate = menuRepository.findById(menuId).orElseThrow();

        ResultActions response = perform(patchRequest("/api/admin/menus/" + menuId).content(buildJSON(updateDto)))
                .andExpect(status().isOk());
        MenuResponseDTO responseMenu = parseJsonResponse(response, MenuResponseDTO.class);

        assertEquals(menuId, responseMenu.id());
        assertEquals(updateDto.date(), responseMenu.date());
        assertEquals(menuBeforeUpdate.getDishes(), responseMenu.dishes());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void update__onlyDishes() throws Exception {
        Long menuId = 1L;
        MenuUpdateDTO updateDto = new MenuUpdateDTO(null, menuUpdateDto.dishes());
        Menu menuBeforeUpdate = menuRepository.findById(menuId).orElseThrow();

        ResultActions response = perform(patchRequest("/api/admin/menus/" + menuId).content(buildJSON(updateDto)))
                .andExpect(status().isOk());
        MenuResponseDTO responseMenu = parseJsonResponse(response, MenuResponseDTO.class);

        assertEquals(menuId, responseMenu.id());
        assertEquals(updateDto.dishes(), responseMenu.dishes());
        assertEquals(menuBeforeUpdate.getDate(), responseMenu.date());
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void update__unauthorized() throws Exception {
        perform(patchRequest("/api/admin/menus/1").content(buildJSON(menuUpdateDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void update__nonexistent() throws Exception {
        perform(patchRequest("/api/admin/menus/99999").content(buildJSON(menuUpdateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void delete() throws Exception {
        Long menuId = 1L;
        assertTrue(menuRepository.existsById(menuId));

        perform(deleteRequest("/api/admin/menus/" + menuId)).andExpect(status().isNoContent());
        assertFalse(menuRepository.existsById(menuId));
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void delete__unauthorized() throws Exception {
        perform(deleteRequest("/api/admin/menus/1")).andExpect(status().isForbidden());
    }
}