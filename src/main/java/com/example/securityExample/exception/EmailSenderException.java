package com.example.securityExample.exception;

public class EmailSenderException extends RuntimeException {

    public EmailSenderException(String message) {
        super("Can not send activation email. " + message);
    }
}
