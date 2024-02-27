package com.example.server2.model;

import com.example.server2.adapter.LocalDateAdapter;
import com.example.server2.adapter.LocalDateTimeAdapter;
import com.example.server2.adapter.ZonedDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "WorkerInfo")
public class WorkerInfo {

    @XmlElement(name = "id")
    private Integer id;

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
        w.setId(worker.getId());
        w.setCoordinate(worker.getCoordinate());
        w.setOrganization(worker.getOrganization());
        w.setSalary(worker.getSalary());
        w.setName(worker.getName());
        w.setPosition(worker.getPosition());
        w.setEndDate(worker.getEndDate());
        w.setStartDate(worker.getStartDate());

        LocalDateTime moscowTime = worker.getCreationDate();
        ZonedDateTime moscowZonedDateTime = moscowTime.atZone(ZoneId.of("Europe/Moscow"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String formattedDate = moscowZonedDateTime.format(formatter);

        w.setCreationDate(ZonedDateTime.parse(formattedDate));

        return w;
    }
    public static ZonedDateTime convertLocalTime(LocalDateTime localDateTime){
        ZoneId targetZoneId = ZoneId.of("Europe/Moscow");
        ZonedDateTime zonedDateTime = localDateTime.atZone(targetZoneId);
        return zonedDateTime;
    }
}
