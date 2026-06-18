package com.deep.smartinventoryandordermanagementsystem.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(),
                                error.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(DuplicateSkuException.class)
    public Map<String, String> handleDuplicateSku(DuplicateSkuException ex){
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(
            DuplicateBarcodeException.class)
    public Map<String, String>
    handleDuplicateBarcode(
            DuplicateBarcodeException ex){

        return Map.of(
                "error",
                ex.getMessage()
        );
    }
}
