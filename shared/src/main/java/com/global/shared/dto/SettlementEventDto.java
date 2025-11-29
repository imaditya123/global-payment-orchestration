package com.global.shared.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.global.shared.enums.SettlementStatus;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public final class SettlementEventDto {
    private final UUID settlementId;
    private final UUID paymentId;
    private final BigDecimal amount;
    private final SettlementStatus status;

    @JsonCreator
    public SettlementEventDto(
            @JsonProperty("settlementId") UUID settlementId,
            @JsonProperty("paymentId") UUID paymentId,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("status") SettlementStatus status) {

        this.settlementId = Objects.requireNonNull(settlementId);
        this.paymentId = Objects.requireNonNull(paymentId);
        this.amount = Objects.requireNonNull(amount);
        this.status = Objects.requireNonNull(status);
    }

    public UUID getSettlementId() { return settlementId; }
    public UUID getPaymentId() { return paymentId; }
    public BigDecimal getAmount() { return amount; }
    public SettlementStatus getStatus() { return status; }
}
