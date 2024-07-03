package com.moonspoon.moonspoon.exception;

public class JWTTokenExpiredException extends RuntimeException{
    public JWTTokenExpiredException(String message) {super(message);}
}
