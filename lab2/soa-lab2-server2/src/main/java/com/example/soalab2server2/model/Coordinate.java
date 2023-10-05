package com.example.soalab2server2.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Coordinate")
public class Coordinate {

    private Long x;

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
