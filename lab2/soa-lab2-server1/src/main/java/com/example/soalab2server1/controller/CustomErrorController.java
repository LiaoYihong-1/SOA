package com.example.soalab2server1.controller;

import com.example.soalab2server1.dao.model.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller @RequiredArgsConstructor
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public ResponseEntity<?> handleError(WebRequest webRequest) {

        String errorMessage = "HTTP method is not supported";
        HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        return ResponseEntity.status(httpStatus)
                .contentType(MediaType.APPLICATION_XML)
                .body(new Error(errorMessage, httpStatus.value()));
    }

}