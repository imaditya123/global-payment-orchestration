package com.global.shared.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.global.shared.enums.PaymentStatus;

import java.util.Objects;
import java.util.UUID;

public final class PaymentResponse {
    private final UUID paymentId;
    private final PaymentStatus status;
    private final String reason;

    @JsonCreator
    public PaymentResponse(
            @JsonProperty("paymentId") UUID paymentId,
            @JsonProperty("status") PaymentStatus status,
            @JsonProperty("reason") String reason) {

        this.paymentId = Objects.requireNonNull(paymentId);
        this.status = Objects.requireNonNull(status);
        this.reason = reason;
    }

    public UUID getPaymentId() { return paymentId; }
    public PaymentStatus getStatus() { return status; }
    public String getReason() { return reason; }
}
