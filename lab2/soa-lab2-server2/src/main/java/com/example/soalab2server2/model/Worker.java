package com.example.soalab2server2.model;


import com.example.soalab2server2.model.Coordinate;
import com.example.soalab2server2.model.Organization;
import com.example.soalab2server2.model.Position;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
@Data
@NoArgsConstructor
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
