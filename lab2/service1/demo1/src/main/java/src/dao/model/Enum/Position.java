package src.dao.model.Enum;

import lombok.Getter;

import java.io.Serializable;

@Getter
public enum Position implements Serializable {
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
