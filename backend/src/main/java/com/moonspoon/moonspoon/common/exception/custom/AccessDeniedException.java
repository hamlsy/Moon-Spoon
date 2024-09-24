package com.moonspoon.moonspoon.common.exception.custom;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message){
        super(message);
    }
}
