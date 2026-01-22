package com.dairyncia.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus {
    PENDING(0, "Pending"),
    PAID(1, "Paid"),
    CANCELLED(2, "Cancelled");

    private final int value;
    private final String displayName;

    PaymentStatus(int value, String displayName) {
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

    public static PaymentStatus fromValue(int value) {
        for (PaymentStatus status : values()) {
            if (status.value == value) return status;
        }
        throw new IllegalArgumentException("Invalid PaymentStatus: " + value);
    }
}