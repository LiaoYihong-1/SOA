package com.example.server2.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "Coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
public class Coordinate {
    @XmlElement
    private Long x;
    @XmlElement
    private double y;
    public Coordinate (Long x, double y){
        this.x = x;
        this.y = y;
    }
}
