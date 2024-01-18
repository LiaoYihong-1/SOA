package src.model;

import jakarta.xml.bind.annotation.XmlEnumValue;
import lombok.Getter;

import java.io.Serializable;

@Getter
public enum Position implements Serializable {
    @XmlEnumValue("MANAGER")
    MANAGER("MANAGER"),
    @XmlEnumValue("DEVELOPER")
    DEVELOPER("DEVELOPER"),
    @XmlEnumValue("HUMAN_RESOURCES")
    HUMAN_RESOURCES("HUMAN_RESOURCES"),
    @XmlEnumValue("HEAD_OF_DEPARTMENT")
    HEAD_OF_DEPARTMENT("HEAD_OF_DEPARTMENT"),
    @XmlEnumValue("COOK")
    COOK("COOK");

    private final String value;
    Position(String value) {
        this.value = value;
    }
}
