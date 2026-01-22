
package com.dairyncia.enums;

import com.fasterxml.jackson.annotation.JsonValue;

// CRITICAL: Match .NET enum values exactly
public enum MilkType {
    COW(1, "Cow"),
    BUFFALO(2, "Buffalo");

    private final int value;
    private final String displayName;

    MilkType(int value, String displayName) {
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

    public static MilkType fromValue(int value) {
        for (MilkType type : values()) {
            if (type.value == value) return type;
        }
        throw new IllegalArgumentException("Invalid MilkType: " + value);
    }
}