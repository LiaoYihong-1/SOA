package com.example.soalab2server2.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
@Data
@NoArgsConstructor
@XmlRootElement(name = "workerFullInfo")
public class Worker {
    private Integer id;

    private String name;

    private Coordinate coordinate;

    private ZonedDateTime creationDate;

    private float salary;

    private LocalDateTime startDate;

    private LocalDate endDate;

    private Position position;

    private Organization organization;


}
