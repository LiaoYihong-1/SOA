package com.example.soalab2server2.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum Position {
    @XmlEnumValue("MANAGER")
    MANAGER,
    @XmlEnumValue("DEVELOPER")
    DEVELOPER,
    @XmlEnumValue("HUMAN_RESOURCES")
    HUMAN_RESOURCES,
    @XmlEnumValue("HEAD_OF_DEPARTMENT")
    HEAD_OF_DEPARTMENT,
    @XmlEnumValue("COOK")
    COOK
}
