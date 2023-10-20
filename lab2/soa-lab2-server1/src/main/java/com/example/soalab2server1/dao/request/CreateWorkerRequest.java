package com.example.soalab2server1.dao.request;

import com.example.soalab2server1.dao.model.Coordinate;
import com.example.soalab2server1.dao.model.Organization;
import com.example.soalab2server1.dao.model.Position;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data @RequiredArgsConstructor @AllArgsConstructor
@JacksonXmlRootElement(localName = "CreateWorkerRequest")
public class CreateWorkerRequest implements Serializable {

    @JacksonXmlCData
    @JacksonXmlProperty(localName = "name")
    @Length(min=1, max=256)
    @NotBlank
    private String name;

//    @JacksonXmlProperty(localName = "Coordinate")
//    private Coordinate coordinate;
//
//    @JacksonXmlProperty(localName = "creationDate")
//    @NotNull
//    @CreatedDate
//    private ZonedDateTime creationDate;
//
//    @JacksonXmlProperty(localName = "salary")
//    @PositiveOrZero
//    @NotNull
//    private float salary;
//
//    @JacksonXmlProperty(localName = "startDate")
//    @NotNull
//    private LocalDateTime startDate;
//
//    @JacksonXmlProperty(localName = "endDate")
//    @NotNull
//    private LocalDate endDate;
//
//    @JacksonXmlProperty(localName = "position")
//    @Enumerated(EnumType.STRING)
//    @NotNull
//    private Position position;
//
//    @JacksonXmlProperty(localName = "Organization")
//    @NotNull
//    private Organization organization;
}
