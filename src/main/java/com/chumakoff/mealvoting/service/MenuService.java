package com.chumakoff.mealvoting.service;

import com.chumakoff.mealvoting.dto.MenuUpdateDTO;
import com.chumakoff.mealvoting.model.Menu;
import com.chumakoff.mealvoting.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.chumakoff.mealvoting.helper.RepositoryHelper.getOrThrow;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    @Transactional
    public Menu update(Long menuId, MenuUpdateDTO dto) {
        Menu menu = getOrThrow(menuRepository.findById(menuId), menuId, Menu.class);
        if (dto.menuDate() != null) menu.setMenuDate(dto.menuDate());
        if (dto.dishes() != null) menu.setDishes(dto.dishes());

        return menuRepository.save(menu);
    }
}
