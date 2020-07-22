package com.example.securityExample.exception;

public class ActivationTokenNotFoundException extends RuntimeException {

    public ActivationTokenNotFoundException(String value) {
        super("Token not found: " + value);
    }
}
