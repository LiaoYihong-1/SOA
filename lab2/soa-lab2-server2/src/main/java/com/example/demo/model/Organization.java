package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@NoArgsConstructor
@XmlRootElement(name = "Organization")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Organization {
    @XmlElement(name="id")
    public Integer id;
    @XmlElement(name="fullName")
    public String fullName;
    @XmlElement(name="annualTurnover")
    public Long annualTurnover;
}
