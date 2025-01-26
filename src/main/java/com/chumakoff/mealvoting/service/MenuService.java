package com.chumakoff.mealvoting.service;

import com.chumakoff.mealvoting.dto.MenuCreateDTO;
import com.chumakoff.mealvoting.dto.MenuUpdateDTO;
import com.chumakoff.mealvoting.dto.MenuValidateDTO;
import com.chumakoff.mealvoting.exception.InvalidRecordException;
import com.chumakoff.mealvoting.model.Menu;
import com.chumakoff.mealvoting.model.Restaurant;
import com.chumakoff.mealvoting.repository.MenuRepository;
import com.chumakoff.mealvoting.repository.RestaurantRepository;
import com.chumakoff.mealvoting.validation.MenuValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import static com.chumakoff.mealvoting.helper.RepositoryHelper.getOrThrow;
import static com.chumakoff.mealvoting.validation.Utils.validateRecord;

@Service
@Transactional
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    private final MenuValidator validator;

    public Menu create(MenuCreateDTO dto) {
        Restaurant restaurant = getOrThrow(restaurantRepository.findById(dto.restaurantId()), dto.restaurantId(), Restaurant.class);
        Menu menu = new Menu(dto.menuDate(), restaurant, dto.dishes());
        validateMenu(MenuValidateDTO.buildFromEntity(menu));
        return menuRepository.save(menu);
    }

    public Menu update(Long menuId, MenuUpdateDTO dto) {
        Menu menu = getOrThrow(menuRepository.findById(menuId), menuId, Menu.class);

        if (dto.menuDate() != null) {
            var validatedMenu = MenuValidateDTO.buildFromEntity(menu);
            validatedMenu.setMenuDate(dto.menuDate());
            validateMenu(validatedMenu);
        }

        if (dto.menuDate() != null) menu.setMenuDate(dto.menuDate());
        if (dto.dishes() != null) menu.setDishes(dto.dishes());

        return menuRepository.save(menu);
    }

    private void validateMenu(MenuValidateDTO menu) {
        Errors errors = validateRecord(menu, validator);
        if (errors.hasErrors()) throw new InvalidRecordException(errors);
    }
}
