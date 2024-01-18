package com.example.soalab2server1.dao.model.Enum;

import lombok.Getter;

@Getter
public enum Position {
            MANAGER("MANAGER"),
            HUMAN_RESOURCES("HUMAN_RESOURCES"),
            HEAD_OF_DEPARTMENT("HEAD_OF_DEPARTMENT"),
            DEVELOPER("DEVELOPER"),
            COOK("COOK");
    private final String value;
    Position(String value) {
        this.value = value;
    }

}
