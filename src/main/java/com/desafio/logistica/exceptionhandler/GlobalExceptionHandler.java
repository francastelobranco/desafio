package com.desafio.logistica.exceptionhandler;


import com.desafio.logistica.exeption.ErrorFileException;
import com.desafio.logistica.exeption.OrderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.desafio.logistica.utils.ConstantsUtils.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleOrderNotFound(
            OrderNotFoundException ex,
            HttpServletRequest request
    ) {
        var bodyResponse = bodyResponseError(ex.getMessage(), request, HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bodyResponse);
    }

    @ExceptionHandler(ErrorFileException.class)
    public ResponseEntity<Map<String, Object>> errorFile(
            ErrorFileException ex,
            HttpServletRequest request
    ) {
        var bodyResponse = bodyResponseError(ex.getMessage(), request, HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bodyResponse);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> maxUploadSizeExceeded(
            MaxUploadSizeExceededException ex,
            HttpServletRequest request
    ) {
        var bodyResponse = bodyResponseError(ex.getMessage(), request, HttpStatus.PAYLOAD_TOO_LARGE);

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(bodyResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> exceptionGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        var bodyResponse = bodyResponseError(ERROR_500, request, HttpStatus.INTERNAL_SERVER_ERROR);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(bodyResponse);
    }

    private Map<String, Object> bodyResponseError(String message, HttpServletRequest request, HttpStatus httpStatus) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIME_STAMP, LocalDateTime.now());
        body.put(STATUS, httpStatus.value());
        body.put(ERROR, httpStatus);
        body.put(MESSAGE, message);
        body.put(PATH, request.getRequestURI());

        return body;
    }
}
