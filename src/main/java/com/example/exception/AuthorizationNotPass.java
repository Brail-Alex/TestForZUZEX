package com.example.exception;

public class AuthorizationNotPass extends Throwable{
    public AuthorizationNotPass(String message) {
        super(message);
    }
}
