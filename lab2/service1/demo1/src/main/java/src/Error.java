package src;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;


@Data
@XmlRootElement(name = "Error")
@XmlAccessorType(XmlAccessType.FIELD)
public class Error {
    @XmlElement
    private String message;
    @XmlElement
    private Integer code;
}
