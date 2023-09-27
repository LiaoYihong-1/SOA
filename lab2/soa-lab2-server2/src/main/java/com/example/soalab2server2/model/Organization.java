package com.example.soalab2server2.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Organization {
    private Integer id;

    private String fullName;

    private Long annualTurnover;
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
