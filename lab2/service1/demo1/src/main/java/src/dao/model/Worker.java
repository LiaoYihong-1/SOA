package src.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.proxy.HibernateProxy;
import src.dao.model.Enum.Position;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Getter
@Setter
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
    @Column(name = "salary", nullable = false, scale = 2)
    @NotNull
    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
    private double salary;

    @JacksonXmlProperty(localName = "startDate")
    @Column(name = "start_date", nullable = false)
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
            Position tmp = Position.valueOf(this.position);
            if (Arrays.asList(Position.values()).contains(tmp)) {
                return tmp.getValue();
            } else {
                throw new IllegalArgumentException("getPosition1");
            }
        } catch (Exception e) {
            System.out.println("IHateEjb");
        }
        return null;
    }

    public void setPosition(Position position) {
        if (Arrays.asList(Position.values()).contains(position)) {
            this.position = position.getValue();
        } else {
            throw new IllegalArgumentException("setPosition");
        }
    }
    public void setPosition(String position) {
        if (Arrays.asList(Position.values()).contains(Position.valueOf(position))) {
            this.position = position;
        } else {
            throw new IllegalArgumentException("setPosition");
        }
    }
    @ManyToOne
    @JoinColumn(name = "organization_id")
    @JacksonXmlProperty(localName = "Organization")
    private Organization organization;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Worker worker = (Worker) o;
        return getId() != null && Objects.equals(getId(), worker.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public String toString() {
        return "Worker(id=" + this.getId() + ", name=" + this.getName() + ", coordinate=" + this.getCoordinate() + ", creationDate=" + this.getCreationDate() + ", salary=" + this.getSalary() + ", startDate=" + this.getStartDate() + ", endDate=" + this.getEndDate() + ", position=" + this.getPosition() + ", organization=" + this.getOrganization() + ")";
    }
}
