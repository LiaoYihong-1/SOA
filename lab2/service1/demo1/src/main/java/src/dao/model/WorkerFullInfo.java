package src.dao.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "WorkerFullInfo")
public class WorkerFullInfo implements Serializable {

    private Integer id;

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "Coordinates")
    private Coordinate coordinate;

    @JacksonXmlProperty(localName = "creationDate")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime creationDate;

    @JacksonXmlProperty(localName = "salary")
    private float salary;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JacksonXmlProperty(localName = "startDate")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JacksonXmlProperty(localName = "endDate")
    private LocalDate endDate;

    @JacksonXmlProperty(localName = "position")
    private String position;

    @JacksonXmlProperty(localName = "Organization")
    private Organization organization;
}
