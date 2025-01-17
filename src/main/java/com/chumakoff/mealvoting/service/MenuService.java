package com.chumakoff.mealvoting.service;

import com.chumakoff.mealvoting.dto.MenuUpdateDTO;
import com.chumakoff.mealvoting.exception.RecordNotFoundException;
import com.chumakoff.mealvoting.model.Menu;
import com.chumakoff.mealvoting.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    @Transactional
    public Menu update(Long menuId, MenuUpdateDTO dto) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new RecordNotFoundException(menuId, Menu.class));
        if (dto.date() != null) menu.setDate(dto.date());
        if (dto.dishes() != null) menu.setDishes(dto.dishes());

        return menuRepository.save(menu);
    }
}
