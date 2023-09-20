package com.example.soalab2server1.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Table(name = "organization")
@Data
@Entity
@NoArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "annual_turnover", nullable = false)
    private Long annualTurnover;

    @OneToMany(mappedBy = "organization")
    private List<Worker> workers;
}
