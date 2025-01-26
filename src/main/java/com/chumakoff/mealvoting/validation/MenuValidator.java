package com.chumakoff.mealvoting.validation;

import com.chumakoff.mealvoting.dto.MenuValidateDTO;
import com.chumakoff.mealvoting.repository.MenuRepository;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MenuValidator implements Validator {
    private final MenuRepository menuRepository;

    public MenuValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return MenuValidateDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        var menu = (MenuValidateDTO) target;
        validateUniqueMenuDatePerRestaurant(menu, errors);
    }

    private void validateUniqueMenuDatePerRestaurant(MenuValidateDTO menu, Errors errors) {
        if (menu.getMenuDate() == null || menu.getRestaurant() == null || menu.getRestaurant().getId() == null) {
            return;
        }

        boolean isDuplicate = (menu.getId() != null) ?
                menuRepository.existsByMenuDateAndRestaurantIdAndIdIsNot(menu.getMenuDate(), menu.getRestaurant().getId(), menu.getId()) :
                menuRepository.existsByMenuDateAndRestaurantId(menu.getMenuDate(), menu.getRestaurant().getId());

        if (isDuplicate) {
            errors.rejectValue("menuDate", "validation.menu.UniqueDatePerRestaurant.message");
        }
    }
}
