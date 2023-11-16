package com.example.soalab2server1.dao.request;

import com.example.soalab2server1.dao.model.Coordinate;
import com.example.soalab2server1.dao.model.Organization;
import com.example.soalab2server1.dao.model.Position;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data @RequiredArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "WorkerInfo")
public class WorkerInfo {

    @JacksonXmlProperty(localName = "name")
    @Length(min=1, max=256)
    @NotBlank
    private String name;

    @JacksonXmlProperty(localName = "Coordinates")
    private Coordinate coordinate;

    @JacksonXmlProperty(localName = "creationDate")
    @NotNull
    private ZonedDateTime creationDate;

    @JacksonXmlProperty(localName = "salary")
    @PositiveOrZero
    @NotNull
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

    @JacksonXmlProperty(localName = "Organization")
    private Organization organization;
}
