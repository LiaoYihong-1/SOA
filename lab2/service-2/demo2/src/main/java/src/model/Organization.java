package src.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@XmlRootElement(name = "Organization")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Organization {
    @XmlElement(name="id")
    public Integer id;
    @XmlElement(name="fullName")
    public String fullName;
    @XmlElement(name="annualTurnover")
    public Long annualTurnover;
}
