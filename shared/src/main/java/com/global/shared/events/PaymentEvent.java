package com.global.shared.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class PaymentEvent {
    private final UUID paymentId;
    private final PaymentEventType type;
    private final Map<String, Object> payload;
    private final EventMetadata metadata;

    @JsonCreator
    public PaymentEvent(
            @JsonProperty("paymentId") UUID paymentId,
            @JsonProperty("type") PaymentEventType type,
            @JsonProperty("payload") Map<String, Object> payload,
            @JsonProperty("metadata") EventMetadata metadata) {

        this.paymentId = Objects.requireNonNull(paymentId);
        this.type = Objects.requireNonNull(type);
        this.payload = Objects.requireNonNull(payload);
        this.metadata = Objects.requireNonNull(metadata);
    }

    public UUID getPaymentId() { return paymentId; }
    public PaymentEventType getType() { return type; }
    public Map<String, Object> getPayload() { return payload; }
    public EventMetadata getMetadata() { return metadata; }
}
