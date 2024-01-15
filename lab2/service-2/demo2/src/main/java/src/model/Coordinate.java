package src.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlRootElement(name = "Coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Coordinate {

    @XmlElement
    private Long x;
    @XmlElement
    private double y;

}
