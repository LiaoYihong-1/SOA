package src.dao.model;

import src.dao.model.Enum.Position;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;

@Slf4j
@Data
@Entity
@Table(name = "worker")
@JacksonXmlRootElement(localName = "WorkerFullInfo")
@AllArgsConstructor
@RequiredArgsConstructor
public class Worker implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JacksonXmlProperty(localName = "id")
    private Integer id;


    @JacksonXmlProperty(localName = "name")
    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "coordinates_x")),
            @AttributeOverride(name = "y", column = @Column(name = "coordinates_y")),
    })
    @JacksonXmlProperty(localName = "Coordinate")
    private Coordinate coordinate;

    @JacksonXmlProperty(localName = "creationDate")
    @Column(name = "creation_date", nullable = false)
    @NotNull
    private ZonedDateTime creationDate;


    @JacksonXmlProperty(localName = "salary")
    @Column(name = "salary", nullable = false,scale = 2)
    @NotNull
    @Digits(integer = Integer.MAX_VALUE , fraction = 2)
    private double salary;

    @JacksonXmlProperty(localName = "startDate")
    @Column(name = "start_date",nullable = false)
    @NotNull
    private LocalDateTime startDate;

    @JacksonXmlProperty(localName = "endDate")
    @Column(name = "end_date", nullable = false)
    @NotNull
    private LocalDate endDate;

    @JacksonXmlProperty(localName = "position")
    @Column(name = "position", nullable = false)
    @NotNull
    private String position;
    public String getPosition() {
        try {
            Position tmp = Position.valueOf(position);
            if (Arrays.asList(Position.values()).contains(tmp)) {
                return tmp.getValue();
            } else {
                throw new IllegalArgumentException("");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("");
        }
    }

    public void setPosition(Position position) {
            if (Arrays.asList(Position.values()).contains(position)) {
                this.position = position.getValue();
            } else {
                throw new IllegalArgumentException("");
            }
    }

    @ManyToOne
    @JoinColumn(name = "organization_id")
    @JacksonXmlProperty(localName = "Organization")
    private Organization organization;
}
