package com.desafio.logistica.exeption;

import static com.desafio.logistica.utils.ConstantsUtils.ERROR_FILE_READ;

public class ErrorFileException extends RuntimeException{

    public ErrorFileException(String fileName) {
        super(String.format(ERROR_FILE_READ, fileName));
    }
}
