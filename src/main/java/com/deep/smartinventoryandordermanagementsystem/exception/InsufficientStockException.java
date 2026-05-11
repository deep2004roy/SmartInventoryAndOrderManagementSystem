package com.deep.smartinventoryandordermanagementsystem.exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String message){
        super(message);
    }
}
