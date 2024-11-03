package com.mprtcz.statusholder.dto;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PaymentStatus {
    STARTED("started"),
    SUCCESS("success"),
    ERROR("error"),
    MISSING("missing"),
    INVALID("invalid");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    public static PaymentStatus fromStatus(String status) {
        return Arrays.stream(PaymentStatus.values())
                .filter(statusString -> statusString.getStatus().equals(status))
                .findFirst()
                .orElse(MISSING);
    }
}
