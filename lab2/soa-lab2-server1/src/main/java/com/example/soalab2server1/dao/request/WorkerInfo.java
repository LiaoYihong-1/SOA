package com.example.soalab2server1.dao.request;

import com.example.soalab2server1.dao.model.Coordinate;
import com.example.soalab2server1.dao.model.Organization;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data @RequiredArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "WorkerInfo")
public class WorkerInfo {

    @JacksonXmlProperty(localName = "name")
    @Length(min=1, max=256)
    @NotBlank
    private String name;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "Coordinates")
    private Coordinate coordinate;

    @JacksonXmlProperty(localName = "creationDate")
    @NotNull
    private ZonedDateTime creationDate;

    @JacksonXmlProperty(localName = "salary")
    @PositiveOrZero
    @NotNull
    @Digits(integer = Integer.MAX_VALUE , fraction = 2)
    private float salary;

    @JacksonXmlProperty(localName = "startDate")
    @NotNull
    private LocalDate startDate;

    @JacksonXmlProperty(localName = "endDate")
    @NotNull
    private LocalDate endDate;

    @JacksonXmlProperty(localName = "position")
    @NotBlank
    private String position;

    @Valid
    @JacksonXmlProperty(localName = "Organization")
    private Organization organization;
}
