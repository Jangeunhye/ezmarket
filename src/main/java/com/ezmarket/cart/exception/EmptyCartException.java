package com.ezmarket.cart.exception;

public class EmptyCartException extends RuntimeException{
    public EmptyCartException(String message){
        super(message);
    }
}
