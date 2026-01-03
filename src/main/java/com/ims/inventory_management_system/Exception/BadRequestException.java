package com.ims.inventory_management_system.Exception;

public class BadRequestException extends RuntimeException{
    
    public BadRequestException(String message){
        super(message);
    }
    
}
