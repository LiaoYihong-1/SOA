package com.example.soalab2server1.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "Coordinate")
public class Coordinate {
    @Column(name = "coordinates_x")
    @JacksonXmlProperty(localName = "coordinates_x")
    private Long x;
    @Column(name = "coordinates_y")
    @JacksonXmlProperty(localName = "coordinates_y")
    private double y;
}
