package com.desafio.logistica.exeption;

import static com.desafio.logistica.utils.ConstantsUtils.ERROR_DATE_INTERVAL_INVALID;

public class InvalidDateIntervalException extends RuntimeException{

    public InvalidDateIntervalException() {
        super(ERROR_DATE_INTERVAL_INVALID);
    }
}
