package com.example.soalab2server1.dao.request;

import com.example.soalab2server1.dao.model.Coordinate;
import com.example.soalab2server1.dao.model.Organization;
import com.example.soalab2server1.dao.model.Enum.Position;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;


@Data @RequiredArgsConstructor @AllArgsConstructor
@JacksonXmlRootElement(localName = "CreateWorkerRequest")
public class CreateWorkerRequest {

    @JacksonXmlProperty(localName = "name")
    @Length(min=1, max=256)
    @NotBlank
    private String name;

    @Valid
    @JacksonXmlProperty(localName = "Coordinates")
    @NotNull
    private Coordinate coordinate;

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
    @NotNull
    private String position;

    @Valid
    @JacksonXmlProperty(localName = "Organization")
    private Organization organization;
}
