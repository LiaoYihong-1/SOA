package com.example.server2.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlRootElement(name = "Coordinate")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Coordinate {

    @XmlElement
    private Long x;
    @XmlElement
    private double y;

}
