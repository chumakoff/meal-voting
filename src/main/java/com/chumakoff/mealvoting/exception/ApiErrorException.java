package com.chumakoff.mealvoting.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class ApiErrorException extends ResponseStatusException {
    public ApiErrorException(HttpStatus status, String message) {
        super(status, message);
    }

    @Override
    @NonNull
    public String getMessage() {
        return getReason();
    }
}
