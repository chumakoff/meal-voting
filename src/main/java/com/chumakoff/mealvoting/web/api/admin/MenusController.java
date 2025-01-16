package com.chumakoff.mealvoting.web.api.admin;

import com.chumakoff.mealvoting.dto.MenuCreateDTO;
import com.chumakoff.mealvoting.dto.MenuResponseDTO;
import com.chumakoff.mealvoting.dto.MenuUpdateDTO;
import com.chumakoff.mealvoting.model.Menu;
import com.chumakoff.mealvoting.model.Restaurant;
import com.chumakoff.mealvoting.repository.MenuRepository;
import com.chumakoff.mealvoting.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController("AdminMenusController")
@RequestMapping("/api/admin/menus")
@RequiredArgsConstructor
@Tag(name = "[ADMIN] Menus")
public class MenusController {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new daily lunch menu for a restaurant.")
    public MenuResponseDTO create(@Valid @RequestBody MenuCreateDTO dto) {
        // TODO exception
        Restaurant restaurant = restaurantRepository.findById(dto.restaurantId()).orElseThrow();
        Menu menu = menuRepository.save(new Menu(dto.date(), restaurant, dto.dishes()));
        return MenuResponseDTO.buildFromEntity(menu);
    }

    @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a lunch menu.")
    public MenuResponseDTO update(@PathVariable("id") Long id, @Valid @RequestBody MenuUpdateDTO dto) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (dto.date() != null) menu.setDate(dto.date());
        if (dto.dishes() != null) menu.setDishes(dto.dishes());

        Menu updatedMenu = menuRepository.save(menu);
        return MenuResponseDTO.buildFromEntity(updatedMenu);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a lunch menu.")
    public void delete(@PathVariable Long id) {
        menuRepository.deleteById(id);
    }
}
