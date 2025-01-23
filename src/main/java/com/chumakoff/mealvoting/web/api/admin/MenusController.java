package com.chumakoff.mealvoting.web.api.admin;

import com.chumakoff.mealvoting.dto.MenuCreateDTO;
import com.chumakoff.mealvoting.dto.MenuResponseDTO;
import com.chumakoff.mealvoting.dto.MenuUpdateDTO;
import com.chumakoff.mealvoting.dto.MenuWithRestaurantResponseDTO;
import com.chumakoff.mealvoting.model.Menu;
import com.chumakoff.mealvoting.model.Restaurant;
import com.chumakoff.mealvoting.repository.MenuRepository;
import com.chumakoff.mealvoting.repository.RestaurantRepository;
import com.chumakoff.mealvoting.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.chumakoff.mealvoting.helper.RepositoryHelper.getOrThrow;

@RestController("AdminMenusController")
@RequestMapping("/api/admin/menus")
@RequiredArgsConstructor
@Tag(name = "[ADMIN] Menus")
public class MenusController {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuService menuService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new daily lunch menu for a restaurant.")
    public MenuWithRestaurantResponseDTO create(@Valid @RequestBody MenuCreateDTO dto) {
        Restaurant restaurant = getOrThrow(restaurantRepository.findById(dto.restaurantId()), dto.restaurantId(), Restaurant.class);
        Menu menu = menuRepository.save(new Menu(dto.menuDate(), restaurant, dto.dishes()));
        return MenuWithRestaurantResponseDTO.buildFromEntity(menu);
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a lunch menu.")
    public MenuResponseDTO update(@PathVariable("id") Long id, @Valid @RequestBody MenuUpdateDTO dto) {
        Menu updatedMenu = menuService.update(id, dto);
        return MenuResponseDTO.buildFromEntity(updatedMenu);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a lunch menu.")
    public void delete(@PathVariable Long id) {
        menuRepository.deleteById(id);
    }
}
