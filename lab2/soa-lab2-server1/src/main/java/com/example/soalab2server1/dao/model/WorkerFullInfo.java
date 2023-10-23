package com.example.soalab2server1.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "WorkerFullInfo")
public class WorkerFullInfo {

    private Integer id;

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "Coordinate")
    private Coordinate coordinate;

    @JacksonXmlProperty(localName = "creationDate")
    private ZonedDateTime creationDate;

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
