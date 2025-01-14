package com.chumakoff.mealvoting.utils;

import com.chumakoff.mealvoting.model.Dish;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class MenuDishesAttributeConverter implements AttributeConverter<List<Dish>, String> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Dish> dishes) {
        try {
            return mapper.writeValueAsString(dishes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Dish> convertToEntityAttribute(String json) {
        try {
            return mapper.readerForListOf(Dish.class).readValue(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
