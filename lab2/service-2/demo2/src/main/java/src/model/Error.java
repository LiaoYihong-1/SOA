package src.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.io.Serializable;


@Data
@XmlRootElement(name = "Error")
@XmlAccessorType(XmlAccessType.FIELD)
public class Error implements Serializable {
    @XmlElement
    private String message;
    @XmlElement
    private Integer code;
}
