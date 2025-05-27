package com.desafio.logistica.exeption;

import static com.desafio.logistica.utils.ConstantsUtils.ERROR_ORDER_NOT_FOUND;

public class OrderNotFoundException extends EntityNotFoundException {
    public OrderNotFoundException(Integer orderId) {
        super(String.format(ERROR_ORDER_NOT_FOUND, orderId));
    }
}
