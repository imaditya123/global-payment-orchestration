package com.global.shared.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.global.shared.enums.Currency;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public final class FxConversionRequest {
    private final UUID requestId;
    private final BigDecimal amount;
    private final Currency from;
    private final Currency to;

    @JsonCreator
    public FxConversionRequest(
            @JsonProperty("requestId") UUID requestId,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("from") Currency from,
            @JsonProperty("to") Currency to) {

        this.requestId = Objects.requireNonNull(requestId);
        this.amount = Objects.requireNonNull(amount);
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
    }

    public UUID getRequestId() { return requestId; }
    public BigDecimal getAmount() { return amount; }
    public Currency getFrom() { return from; }
    public Currency getTo() { return to; }
}
