package com.chumakoff.mealvoting.web.api.admin;

import com.chumakoff.mealvoting.dto.MenuCreateDTO;
import com.chumakoff.mealvoting.dto.MenuResponseDTO;
import com.chumakoff.mealvoting.dto.MenuUpdateDTO;
import com.chumakoff.mealvoting.model.Dish;
import com.chumakoff.mealvoting.model.Menu;
import com.chumakoff.mealvoting.repository.MenuRepository;
import com.chumakoff.mealvoting.testsupport.web.api.ApiControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static com.chumakoff.mealvoting.testsupport.web.api.TestDBData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MenusControllerTest extends ApiControllerTest {
    private static final String MENUS_API_ENDPOINT = "/api/admin/menus";

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
        ResultActions response = perform(postRequest(MENUS_API_ENDPOINT).content(buildJSON(menuCreateDto)))
                .andExpect(status().isCreated());
        MenuResponseDTO responseMenu = parseJsonResponse(response, MenuResponseDTO.class);
        assertNotNull(responseMenu.id());
        assertEquals(menuCreateDto.menuDate(), responseMenu.menuDate());
        assertEquals(menuCreateDto.dishes(), responseMenu.dishes());
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void create__unauthorized() throws Exception {
        perform(postRequest(MENUS_API_ENDPOINT).content(buildJSON(menuCreateDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void create__withNonexistentRestaurant() throws Exception {
        MenuCreateDTO createDto = new MenuCreateDTO(99999L, menuCreateDto.menuDate(), menuCreateDto.dishes());
        perform(postRequest(MENUS_API_ENDPOINT).content(buildJSON(createDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Could not find Restaurant with id=99999"));
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void create__invalidMenuDate() throws Exception {
        MenuCreateDTO createDto = new MenuCreateDTO(menuCreateDto.restaurantId(), null, menuCreateDto.dishes());
        perform(postRequest(MENUS_API_ENDPOINT).content(buildJSON(createDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("[menuDate] must not be null"));
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void create__duplicateMenuDatePerRestaurant() throws Exception {
        Menu existingMenu = menuRepository.findById(1L).orElseThrow();
        MenuCreateDTO createDto = new MenuCreateDTO(existingMenu.getRestaurant().getId(), existingMenu.getMenuDate(), menuCreateDto.dishes());
        perform(postRequest(MENUS_API_ENDPOINT).content(buildJSON(createDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("A menu item for the given date already exists"));
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void update() throws Exception {
        Long menuId = 1L;
        ResultActions response = perform(patchRequest(menuUrl(menuId)).content(buildJSON(menuUpdateDto)))
                .andExpect(status().isOk());
        MenuResponseDTO responseMenu = parseJsonResponse(response, MenuResponseDTO.class);

        assertEquals(menuId, responseMenu.id());
        assertEquals(menuUpdateDto.menuDate(), responseMenu.menuDate());
        assertEquals(menuUpdateDto.dishes(), responseMenu.dishes());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void update__onlyDate() throws Exception {
        Long menuId = 1L;
        MenuUpdateDTO updateDto = new MenuUpdateDTO(menuUpdateDto.menuDate(), null);
        Menu menuBeforeUpdate = menuRepository.findById(menuId).orElseThrow();

        ResultActions response = perform(patchRequest(menuUrl(menuId)).content(buildJSON(updateDto)))
                .andExpect(status().isOk());
        MenuResponseDTO responseMenu = parseJsonResponse(response, MenuResponseDTO.class);

        assertEquals(menuId, responseMenu.id());
        assertEquals(updateDto.menuDate(), responseMenu.menuDate());
        assertEquals(menuBeforeUpdate.getDishes(), responseMenu.dishes());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void update__onlyDishes() throws Exception {
        Long menuId = 1L;
        MenuUpdateDTO updateDto = new MenuUpdateDTO(null, menuUpdateDto.dishes());
        LocalDate menuDateBeforeUpdate = menuRepository.findById(menuId).map(Menu::getMenuDate).orElseThrow();

        ResultActions response = perform(patchRequest(menuUrl(menuId)).content(buildJSON(updateDto)))
                .andExpect(status().isOk());
        MenuResponseDTO responseMenu = parseJsonResponse(response, MenuResponseDTO.class);

        assertEquals(menuId, responseMenu.id());
        assertEquals(updateDto.dishes(), responseMenu.dishes());
        assertEquals(menuDateBeforeUpdate, responseMenu.menuDate());
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void update__unauthorized() throws Exception {
        perform(patchRequest(menuUrl(1L)).content(buildJSON(menuUpdateDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void update__nonexistent() throws Exception {
        perform(patchRequest(menuUrl(99999L)).content(buildJSON(menuUpdateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void update__emptyDishes() throws Exception {
        Long menuId = 1L;
        MenuUpdateDTO updateDto = new MenuUpdateDTO(null, List.of());
        ResultActions response = perform(patchRequest(menuUrl(menuId)).content(buildJSON(updateDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("[dishes] must not be empty"));
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void update__duplicateMenuDate() throws Exception {
        Long updatedMenuId = 1L;
        Long anotherMenuId = 2L;
        LocalDate anotherMenuDate = menuRepository.findById(anotherMenuId).map(Menu::getMenuDate).orElseThrow();
        MenuUpdateDTO updateDto = new MenuUpdateDTO(anotherMenuDate, null);

        ResultActions response = perform(patchRequest(menuUrl(updatedMenuId)).content(buildJSON(updateDto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("A menu item for the given date already exists"));
    }

    @Test
    @WithUserDetails(value = AUTH_ADMIN_LOGIN)
    void delete() throws Exception {
        Long menuId = 1L;
        assertTrue(menuRepository.existsById(menuId));

        perform(deleteRequest(menuUrl(menuId))).andExpect(status().isNoContent());
        assertFalse(menuRepository.existsById(menuId));
    }

    @Test
    @WithUserDetails(value = AUTH_USER_LOGIN)
    void delete__unauthorized() throws Exception {
        perform(deleteRequest(menuUrl(1L))).andExpect(status().isForbidden());
    }

    private String menuUrl(Long id) {
        return MENUS_API_ENDPOINT + "/" + id;
    }
}