package com.example.soalab2server1.exception;

import com.example.soalab2server1.dao.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Error message = new Error(
                "The specified resource was not found",
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_XML)
                .body(message);

    }
    @ExceptionHandler(value = {InvalidParameterException.class})
    public ResponseEntity<?> resourceNotFoundException(InvalidParameterException ex, WebRequest request) {
        Error message = new Error(
                "Invalid request",
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_XML)
                .body(message);

    }

    @ExceptionHandler(value = {InvalidConditionException.class})
    public ResponseEntity<?> resourceNotFoundException(InvalidConditionException ex, WebRequest request) {
        Error message = new Error(
                "Invalid request",
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_XML)
                .body(message);

    }

}
