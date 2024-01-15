package src.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Table(name = "organization")
@Data
@Entity
@NoArgsConstructor
@JacksonXmlRootElement(localName = "Organization")
public class Organization implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name", nullable = false)
    @NotNull
    private String fullName;

    @Column(name = "annual_turnover", nullable = false)
    @NotNull
    @Min(0)
    private Long annualTurnover;

}
