package src.dao.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import src.dao.model.Coordinate;
import src.dao.model.Organization;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.time.LocalDate;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "CreateWorkerRequest")
public class CreateWorkerRequest implements Serializable {

    @JacksonXmlProperty(localName = "name")
    @NotBlank
    private String name;

    @Valid
    @JacksonXmlProperty(localName = "Coordinates")
    @NotNull
    private Coordinate coordinate;

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
    @NotNull
    private String position;

    @Valid
    @JacksonXmlProperty(localName = "Organization")
    private Organization organization;
}
