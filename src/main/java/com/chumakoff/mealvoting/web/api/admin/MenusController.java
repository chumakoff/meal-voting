package com.chumakoff.mealvoting.web.api.admin;

import com.chumakoff.mealvoting.dto.MenuCreateDTO;
import com.chumakoff.mealvoting.dto.MenuResponseDTO;
import com.chumakoff.mealvoting.model.Menu;
import com.chumakoff.mealvoting.model.Restaurant;
import com.chumakoff.mealvoting.repository.MenuRepository;
import com.chumakoff.mealvoting.repository.RestaurantRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController(value = "AdminMenusController")
@RequestMapping(value = "/api/admin/menus")
@RequiredArgsConstructor
public class MenusController {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MenuResponseDTO create(@Valid @RequestBody MenuCreateDTO menuDto) {
        Restaurant restaurant = restaurantRepository.getReferenceById(menuDto.restaurantId());
        Menu menu = menuRepository.save(new Menu(menuDto.date(), restaurant, menuDto.dishes()));
        return MenuResponseDTO.buildFromEntity(menu);
    }
}
