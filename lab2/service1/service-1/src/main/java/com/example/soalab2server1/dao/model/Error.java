package com.example.soalab2server1.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JacksonXmlRootElement(localName = "Error")
public class Error {
    public Error(String message,Integer code){
        this.code = code;
        this.message = message;
    }
    public String message;
    public Integer code;
}
