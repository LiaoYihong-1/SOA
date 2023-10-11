package com.example.soalab2server1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidConditionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidConditionException(String msg) {
        super(msg);
    }

}