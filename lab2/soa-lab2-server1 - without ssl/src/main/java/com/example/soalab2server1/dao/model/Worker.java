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
@Table(name = "Worker")
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "coordinates_x")),
            @AttributeOverride(name = "y", column = @Column(name = "coordinates_y")),
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

    @Column(name = "position", nullable = false,columnDefinition = "com.example.soalab2server1.dao.model.Position")
    @Enumerated(EnumType.STRING)
    private Position position;

    /**
     * Here use @JoinColumn so don't need to map organization_id again like other properties!
     */
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}