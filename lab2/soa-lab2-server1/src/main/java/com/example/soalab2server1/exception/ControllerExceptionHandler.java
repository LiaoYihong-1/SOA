package com.example.soalab2server1.exception;

import com.example.soalab2server1.dao.model.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {
            ResourceNotFoundException.class,
            InvalidParameterException.class,
            InvalidConditionException.class,
            javax.validation.ConstraintViolationException.class,
            org.hibernate.TransientPropertyValueException.class,
            java.lang.IllegalArgumentException.class,
            org.springframework.http.converter.HttpMessageNotReadableException.class,
            org.springframework.web.bind.MethodArgumentNotValidException.class,
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<?> handleException(Exception ex, WebRequest request) {
        log.error("Exception occurred", ex);

        String errorMessage = "Invalid request";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        if (ex instanceof ResourceNotFoundException) {
            errorMessage = "The specified resource was not found";
            httpStatus = HttpStatus.NOT_FOUND;
        }

        Error message = new Error(errorMessage, httpStatus.value());

        return ResponseEntity.status(httpStatus)
                .contentType(MediaType.APPLICATION_XML)
                .body(message);
    }
}