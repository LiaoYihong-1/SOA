package com.example.soalab2server1.exception;

import com.example.soalab2server1.dao.model.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice @Slf4j
public class ControllerExceptionHandler {
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.info("ResourceNotFoundException");
        Error message = new Error(
                "The specified resource was not found",
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_XML)
                .body(message);

    }
    @ExceptionHandler(value = {InvalidParameterException.class})
    public ResponseEntity<?> invalidParameterException(InvalidParameterException ex, WebRequest request) {
        log.info("InvalidParameterException");
        Error message = new Error(
                "Invalid request",
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_XML)
                .body(message);

    }

    @ExceptionHandler(value = {InvalidConditionException.class})
    public ResponseEntity<?> invalidConditionException(InvalidConditionException ex, WebRequest request) {
        log.info("InvalidConditionException");
        Error message = new Error(
                "Invalid request",
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_XML)
                .body(message);

    }
    @ExceptionHandler(value = {javax.validation.ConstraintViolationException.class})
    public ResponseEntity<?> constraintViolationException(javax.validation.ConstraintViolationException ex, WebRequest request) {
        log.info("ConstraintViolationException");
        Error message = new Error(
                "Invalid request",
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_XML)
                .body(message);

    }
    @ExceptionHandler(value = {org.springframework.http.converter.HttpMessageNotReadableException.class})
    public ResponseEntity<?> constraintViolationException(org.springframework.http.converter.HttpMessageNotReadableException ex, WebRequest request) {
        log.info("HttpMessageNotReadableException");
        Error message = new Error(
                "Invalid request",
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_XML)
                .body(message);

    }
    @ExceptionHandler(value = {org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> constraintViolationException(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex, WebRequest request) {
        log.info("MethodArgumentTypeMismatchException");
        Error message = new Error(
                "Invalid request",
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_XML)
                .body(message);

    }

}
