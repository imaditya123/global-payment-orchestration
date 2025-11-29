package com.global.shared.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.global.shared.dto.SettlementEventDto;

import java.util.Objects;
import java.util.UUID;

public final class SettlementEvent {
    private final UUID settlementId;
    private final SettlementEventDto payload;
    private final EventMetadata metadata;

    @JsonCreator
    public SettlementEvent(
            @JsonProperty("settlementId") UUID settlementId,
            @JsonProperty("payload") SettlementEventDto payload,
            @JsonProperty("metadata") EventMetadata metadata) {

        this.settlementId = Objects.requireNonNull(settlementId);
        this.payload = Objects.requireNonNull(payload);
        this.metadata = Objects.requireNonNull(metadata);
    }

    public UUID getSettlementId() { return settlementId; }
    public SettlementEventDto getPayload() { return payload; }
    public EventMetadata getMetadata() { return metadata; }
}
