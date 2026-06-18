package com.deep.smartinventoryandordermanagementsystem.exception;

public class DuplicateBarcodeException extends RuntimeException{
    public DuplicateBarcodeException(String message){
        super(message);
    }
}
