package src.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;
import src.adapter.LocalDateAdapter;
import src.adapter.LocalDateTimeAdapter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "WorkerFullInfo")
public class Worker implements Serializable {
    @XmlElement(name = "id")
    private Integer id;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name="Coordinates")
    private Coordinate coordinate;

    @XmlElement(name = "creationDate")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime creationDate;

    @XmlElement(name = "salary")
    private float salary;

    @XmlElement(name = "startDate")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate startDate;

    @XmlElement(name = "endDate")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate endDate;

    @XmlElement(name = "position")
    private String position;

    @XmlElement(name="Organization")
    private Organization organization;
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
}
