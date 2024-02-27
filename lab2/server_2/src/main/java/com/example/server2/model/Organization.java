package com.example.server2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.Serializable;

@NoArgsConstructor
@XmlRootElement(name = "Organization",namespace = "https://localhost:9000/company/worker")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
public class Organization implements Serializable {
    @XmlElement(name="id")
    public Integer id;
    @XmlElement(name="fullName")
    public String fullName;
    @XmlElement(name="annualTurnover")
    public Long annualTurnover;

}
