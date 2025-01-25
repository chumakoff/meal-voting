package com.chumakoff.mealvoting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

import java.util.stream.Collectors;

import static com.chumakoff.mealvoting.validation.Utils.getAllErrorMessages;

public class InvalidRecordException extends ApiErrorException {
    public InvalidRecordException(Errors errors) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, getAllErrorMessages(errors).collect(Collectors.joining()));
    }
}
