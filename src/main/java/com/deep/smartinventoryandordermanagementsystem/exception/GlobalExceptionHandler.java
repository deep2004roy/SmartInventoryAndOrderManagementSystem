package com.deep.smartinventoryandordermanagementsystem.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public Map<String, String> handleProductNotFound(
            ProductNotFoundException ex){

        return Map.of(
                "error", ex.getMessage()
        );
    }

    @ExceptionHandler(InsufficientStockException.class)
    public Map<String, String> handleInsufficientStock(
            InsufficientStockException ex){

        return Map.of(
                "error", ex.getMessage()
        );
    }
}
