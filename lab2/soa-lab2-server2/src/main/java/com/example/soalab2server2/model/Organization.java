package com.example.soalab2server2.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@XmlRootElement(name = "Organization")
@XmlAccessorType(XmlAccessType.FIELD)
public class Organization {
    @XmlElement(name="id")
    public Integer id;
    @XmlElement(name="fullName")
    public String fullName;
    @XmlElement(name="annualTurnover")
    public Long annualTurnover;
    public Integer getId() {
        return id;
    }

    public Long getAnnualTurnover() {
        return annualTurnover;
    }

    public String getFullName() {
        return fullName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAnnualTurnover(Long annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
