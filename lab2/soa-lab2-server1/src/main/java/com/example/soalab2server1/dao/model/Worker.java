package com.example.soalab2server1.dao.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Entity
@NoArgsConstructor
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "x", column = @Column(name = "coordinates_x")),
            @AttributeOverride( name = "y", column = @Column(name = "coordinates_y")),
    })
    private Coordinate coordinate;

    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @Column(name = "salary", nullable = false)
    private float salary;

    @Column(name = "start_date",nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "position", nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(name = "organization_id",nullable = false,insertable = false,updatable = false)
    private Integer organizationId;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
