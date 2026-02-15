package com.susana_garcia.payment_gateway.infraestructure.exception;

public class InsufficientBalanceException extends RuntimeException{

    public InsufficientBalanceException(String message){
        super(message);
    }
}
