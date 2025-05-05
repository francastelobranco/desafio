package com.desafio.logistica.exeption;

public class ErrorFileException extends RuntimeException{

    public ErrorFileException(String file) {
        super(String.format("Error reading file: %s", file));
    }
}
