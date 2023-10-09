package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "WorkerFullInfo")
public class Worker {
    @XmlElement(name = "id")
    private Integer id;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name="Coordinate")
    private Coordinate coordinate;

    @XmlElement(name = "creationDate")
    private ZonedDateTime creationDate;

    @XmlElement(name = "salary")
    private float salary;

    @XmlElement(name = "startDate")
    private LocalDateTime startDate;

    @XmlElement(name = "endDate")
    private LocalDate endDate;

    @XmlElement(name = "position")
    private Position position;

    @XmlElement(name="Organization")
    private Organization organization;


}
