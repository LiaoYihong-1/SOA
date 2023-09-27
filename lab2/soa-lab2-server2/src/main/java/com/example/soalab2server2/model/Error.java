package com.example.soalab2server2.model;

import lombok.Data;

@Data
public class Error {
    private String message;
    private Integer code;

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
