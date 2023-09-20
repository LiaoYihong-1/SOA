package com.example.soalab2server1.dao.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
public class Coordinate {
    @Column(name = "coordinates_x")
    private Long x;
    @Column(name = "coordinates_y")
    private double y;
}
