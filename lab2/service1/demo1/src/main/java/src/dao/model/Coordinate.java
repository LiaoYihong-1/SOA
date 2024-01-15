package src.dao.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "Coordinate")
public class Coordinate implements Serializable {
    @Column(name = "coordinates_x")
    @JacksonXmlProperty(localName = "x")
    @NotNull
    private Long x;

    @Column(name = "coordinates_y",scale = 2)
    @JacksonXmlProperty(localName = "y")
    @NotNull
    @Min(-561)
    @Digits(integer = Integer.MAX_VALUE , fraction = 2)
    private double y;

}
