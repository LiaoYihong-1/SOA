package src.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;
import src.adapter.LocalDateAdapter;
import src.adapter.ZonedDateTimeAdapter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "WorkerInfo")
public class WorkerInfo {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name="Coordinates")
    private Coordinate coordinate;

    @XmlElement(name = "creationDate")
    @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
    private ZonedDateTime creationDate;

    @XmlElement(name = "salary")
    private float salary;

    @XmlElement(name = "startDate")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate startDate;

    @XmlElement(name = "endDate")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate endDate;

    @XmlElement(name = "position")
    private Position position;

    @XmlElement(name="Organization")
    private Organization organization;

    public static WorkerInfo ConvertWorker(Worker worker){
        WorkerInfo w = new WorkerInfo();
        w.setCoordinate(worker.getCoordinate());
        w.setOrganization(worker.getOrganization());
        w.setSalary(worker.getSalary());
        w.setName(worker.getName());
        w.setPosition(worker.getPosition());
        w.setEndDate(worker.getEndDate());
        w.setStartDate(worker.getStartDate());
        w.setCreationDate(convertLocalTime(worker.getCreationDate()));
        return w;
    }
    public static ZonedDateTime convertLocalTime(LocalDateTime localDateTime){
        // 指定目标时区（例如：纽约时区）
        ZoneId targetZoneId = ZoneId.of("Europe/Moscow");

        // 将 LocalDateTime 转换为 ZonedDateTime
        ZonedDateTime zonedDateTime = localDateTime.atZone(targetZoneId);
        return zonedDateTime;
    }
}
