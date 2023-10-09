package com.example.demo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Coordinate")
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinate {

    @XmlElement
    private Long x;
    @XmlElement
    private double y;

    public double getY() {
        return y;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
