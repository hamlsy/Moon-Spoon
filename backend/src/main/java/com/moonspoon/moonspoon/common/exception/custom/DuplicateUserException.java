package com.moonspoon.moonspoon.common.exception.custom;

public class DuplicateUserException extends RuntimeException{
    public DuplicateUserException(String message){
        super(message);
    }
}