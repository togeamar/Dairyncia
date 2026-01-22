package com.dairyncia.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MilkShift {
    MORNING(1, "Morning"),
    EVENING(2, "Evening");

    private final int value;
    private final String displayName;

    MilkShift(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MilkShift fromValue(int value) {
        for (MilkShift shift : values()) {
            if (shift.value == value) return shift;
        }
        throw new IllegalArgumentException("Invalid MilkShift: " + value);
    }
}
