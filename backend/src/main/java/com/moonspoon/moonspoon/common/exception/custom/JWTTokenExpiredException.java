package com.moonspoon.moonspoon.common.exception.custom;

public class JWTTokenExpiredException extends RuntimeException{
    public JWTTokenExpiredException(String message) {super(message);}
}
