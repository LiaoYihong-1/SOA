package com.example.soalab2server1.dao.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "worker")
@JacksonXmlRootElement(localName = "WorkerFullInfo")
@AllArgsConstructor
@RequiredArgsConstructor
public class Worker implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JacksonXmlProperty(localName = "id")
    private Integer id;

    @JacksonXmlProperty(localName = "name")
    @Column(name = "name", nullable = false)
    @Length(min=1, max=256)
    @NotBlank
    private String name;

//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "x", column = @Column(name = "coordinates_x")),
//            @AttributeOverride(name = "y", column = @Column(name = "coordinates_y")),
//    })
//    @JacksonXmlProperty(localName = "Coordinate")
//    private Coordinate coordinate;

    @JacksonXmlProperty(localName = "creationDate")
    @Column(name = "creation_date", nullable = false)
    @NotNull
    private ZonedDateTime creationDate;

    @JacksonXmlProperty(localName = "salary")
    @Column(name = "salary", nullable = false)
    @PositiveOrZero
    @NotNull
    private float salary;

    @JacksonXmlProperty(localName = "startDate")
    @Column(name = "start_date",nullable = false)
    @NotNull
    private LocalDateTime startDate;

    @JacksonXmlProperty(localName = "endDate")
    @Column(name = "end_date", nullable = false)
    @NotNull
    private LocalDate endDate;

    @JacksonXmlProperty(localName = "position")
    @Column(name = "position", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Position position;

//    @ManyToOne
//    @JoinColumn(name = "organization_id")
//    @JacksonXmlProperty(localName = "Organization")
//    @NotNull
//    private Organization organization;
}
