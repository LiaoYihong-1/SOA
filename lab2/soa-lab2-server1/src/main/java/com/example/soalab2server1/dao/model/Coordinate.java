package com.example.soalab2server1.dao.model;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
public class Coordinate {
    private Long x;
    private double y;
}
