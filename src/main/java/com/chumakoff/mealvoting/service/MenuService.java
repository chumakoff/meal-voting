package com.chumakoff.mealvoting.service;

import com.chumakoff.mealvoting.dto.MenuUpdateDTO;
import com.chumakoff.mealvoting.model.Menu;
import com.chumakoff.mealvoting.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    @Transactional
    public Menu update(Long menuId, MenuUpdateDTO dto) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (dto.date() != null) menu.setDate(dto.date());
        if (dto.dishes() != null) menu.setDishes(dto.dishes());

        return menuRepository.save(menu);
    }
}
