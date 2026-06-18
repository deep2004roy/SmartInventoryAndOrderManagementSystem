package com.deep.smartinventoryandordermanagementsystem.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound(
            ProductNotFoundException ex){

        return new ErrorResponse(
                404,
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ErrorResponse handleInsufficientStock(InsufficientStockException ex) {
        return new ErrorResponse(
                400,
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(),
                                error.getDefaultMessage()));
        return new ErrorResponse(
                400,
                "Validation Failed",
                LocalDateTime.now(),
                errors
        );
    }

    @ExceptionHandler(DuplicateSkuException.class)
    public ErrorResponse handleDuplicateSku(DuplicateSkuException ex) {
        return new ErrorResponse(
                409,
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
    }

    @ExceptionHandler(DuplicateBarcodeException.class)
    public ErrorResponse handleDuplicateBarcode(DuplicateBarcodeException ex) {
        return new ErrorResponse(
                409,
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        return new ErrorResponse(
                404,
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ErrorResponse handleInvalidCredentials(InvalidCredentialsException ex) {
        return new ErrorResponse(
                401,
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ErrorResponse handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return new ErrorResponse(
                409,
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
    }
}
