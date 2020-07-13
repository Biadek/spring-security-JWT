package com.example.securityExample.controller;

import com.example.securityExample.dto.ExceptionResponse;
import com.example.securityExample.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(value = {UsernameNotFoundException.class, BookNotFoundException.class, UserNotFoundException.class, ReaderNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFound(Exception ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }


    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse conflict(ConflictException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(NotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ExceptionResponse notAcceptable(ConflictException ex) {
        return new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE.value());
    }

}
