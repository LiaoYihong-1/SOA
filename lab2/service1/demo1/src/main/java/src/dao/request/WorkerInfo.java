package src.dao.request;

import jakarta.validation.constraints.NotBlank;
import src.dao.model.Coordinate;
import src.dao.model.Organization;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data @RequiredArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "WorkerInfo")
public class WorkerInfo implements Serializable {

    @JacksonXmlProperty(localName = "name")
    @NotBlank
    private String name;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "Coordinates")
    private Coordinate coordinate;

    @JacksonXmlProperty(localName = "creationDate")
    @NotNull
    private ZonedDateTime creationDate;

    @JacksonXmlProperty(localName = "salary")
    @NotNull
    @Digits(integer = Integer.MAX_VALUE , fraction = 2)
    private double salary;

    @JacksonXmlProperty(localName = "startDate")
    @NotNull
    private LocalDate startDate;

    @JacksonXmlProperty(localName = "endDate")
    @NotNull
    private LocalDate endDate;

    @JacksonXmlProperty(localName = "position")
    @NotBlank
    private String position;

    @JacksonXmlProperty(localName = "Organization")
    private Organization organization;
}
