package com.chumakoff.mealvoting.validation;

import com.chumakoff.mealvoting.repository.MenuRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class UniqueDatePerRestaurantValidator implements ConstraintValidator<UniqueDatePerRestaurant, Object> {
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public void initialize(UniqueDatePerRestaurant constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        Object date = new BeanWrapperImpl(o).getPropertyValue("date");
        Object restaurantId = new BeanWrapperImpl(o).getPropertyValue("restaurantId");

        if (!menuRepository.existsByDateAndRestaurantId((LocalDate) date, (Long) restaurantId)) {
            return true;
        }

        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("date").addConstraintViolation();
        return false;
    }
}
