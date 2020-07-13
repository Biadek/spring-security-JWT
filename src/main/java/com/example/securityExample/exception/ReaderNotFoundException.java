package com.example.securityExample.exception;

public class ReaderNotFoundException extends RuntimeException {

    public ReaderNotFoundException(Long id) {
        super("Reader not found: " + id);
    }
}
