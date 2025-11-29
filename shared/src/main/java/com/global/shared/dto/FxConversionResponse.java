package com.global.shared.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.global.shared.enums.Currency;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public final class FxConversionResponse {
    private final UUID requestId;
    private final BigDecimal originalAmount;
    private final BigDecimal convertedAmount;
    private final Currency to;
    private final BigDecimal rate;

    @JsonCreator
    public FxConversionResponse(
            @JsonProperty("requestId") UUID requestId,
            @JsonProperty("originalAmount") BigDecimal originalAmount,
            @JsonProperty("convertedAmount") BigDecimal convertedAmount,
            @JsonProperty("to") Currency to,
            @JsonProperty("rate") BigDecimal rate) {

        this.requestId = Objects.requireNonNull(requestId);
        this.originalAmount = Objects.requireNonNull(originalAmount);
        this.convertedAmount = Objects.requireNonNull(convertedAmount);
        this.to = Objects.requireNonNull(to);
        this.rate = Objects.requireNonNull(rate);
    }

    public UUID getRequestId() { return requestId; }
    public BigDecimal getOriginalAmount() { return originalAmount; }
    public BigDecimal getConvertedAmount() { return convertedAmount; }
    public Currency getTo() { return to; }
    public BigDecimal getRate() { return rate; }
}
