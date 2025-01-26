package com.chumakoff.mealvoting.validation;

import com.chumakoff.mealvoting.config.ApplicationContextProvider;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.stream.Stream;

public class Utils {
    public static Stream<String> getAllErrorMessages(Errors errors) {
        var errorItems = errors.getAllErrors();
        if (errorItems.isEmpty()) return Stream.of();

        var context = ApplicationContextProvider.getApplicationContext();
        var messageSource = (MessageSource) context.getBean("messageSource");
        var locale = LocaleContextHolder.getLocale();

        return errorItems.stream().map(error -> messageSource.getMessage(error.getCode(), null, locale));
    }

    public static <T> Errors validateRecord(T record, Validator validator) {
        Errors errors = new BeanPropertyBindingResult(record, "record");
        validator.validate(record, errors);
        return errors;
    }
}
