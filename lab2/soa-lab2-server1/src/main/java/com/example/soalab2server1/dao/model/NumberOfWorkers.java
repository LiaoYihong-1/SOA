package com.example.soalab2server1.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Data
@JacksonXmlRootElement(localName = "NumberOfWorkers")
@AllArgsConstructor
public class NumberOfWorkers {
        private Integer number;
}
