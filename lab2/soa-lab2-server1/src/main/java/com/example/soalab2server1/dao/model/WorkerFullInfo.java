package com.example.soalab2server1.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "WorkerFullInfo")
public class WorkerFullInfo {

    private Integer id;

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "Coordinates")
    private Coordinate coordinate;

    @JacksonXmlProperty(localName = "creationDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime creationDate;

    @JacksonXmlProperty(localName = "salary")
    private float salary;

    @JacksonXmlProperty(localName = "startDate")
    private LocalDate startDate;

    @JacksonXmlProperty(localName = "endDate")
    private LocalDate endDate;

    @JacksonXmlProperty(localName = "position")
    private Position position;

    @JacksonXmlProperty(localName = "Organization")
    private Organization organization;
}
