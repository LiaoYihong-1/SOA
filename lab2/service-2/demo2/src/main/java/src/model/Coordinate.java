package src.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.io.Serializable;

@XmlRootElement(name = "Coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Coordinate implements Serializable {

    @XmlElement
    private Long x;
    @XmlElement
    private double y;

}
