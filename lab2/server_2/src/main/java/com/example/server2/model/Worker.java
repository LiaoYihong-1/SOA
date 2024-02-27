package com.example.server2.model;

import com.example.server2.adapter.LocalDateAdapter;
import com.example.server2.adapter.LocalDateTimeAdapter;
import com.example.server2.adapter.ZonedDateTimeAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "WorkerFullInfo",namespace = "https://localhost:9000/company/worker")
public class Worker {
    @XmlElement(name = "id")
    private Integer id;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name="Coordinates")
    private Coordinate coordinate;

    @XmlElement(name = "creationDate")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime creationDate;

    @XmlElement(name = "salary")
    private float salary;

    @XmlElement(name = "startDate")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate startDate;

    @XmlElement(name = "endDate")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate endDate;

    @XmlElement(name = "position")
    private Position position;

    @XmlElement(name="Organization")
    private Organization organization;

}
