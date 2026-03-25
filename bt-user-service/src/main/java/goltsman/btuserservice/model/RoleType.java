package goltsman.btuserservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum RoleType {
    ADMIN,
    CLIENT;

    @JsonValue
    public String toJson() {
        return name();
    }

    @JsonCreator
    public static RoleType fromString(String value) {
        return Arrays.stream(RoleType.values())
                .filter(role -> role.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестная роль: " + value));
    }
}
