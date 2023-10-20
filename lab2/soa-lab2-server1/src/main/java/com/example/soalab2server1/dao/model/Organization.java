package com.example.soalab2server1.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Table(name = "organization")
@Data
@Entity
@NoArgsConstructor
@JacksonXmlRootElement(localName = "Organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name", nullable = false)
    @NotNull
    @Length(max = 758)
    private String fullName;

    @Column(name = "annual_turnover", nullable = false)
    @NotNull
    @Min(0)
    private Long annualTurnover;

//    @JsonIgnore
//    @OneToMany(mappedBy = "organization")
//    private List<Worker> books;
}
