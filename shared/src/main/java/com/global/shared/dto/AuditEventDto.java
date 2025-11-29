package com.global.shared.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class AuditEventDto {
    private final UUID eventId;
    private final String eventType;
    private final Instant timestamp;
    private final String sourceService;
    private final Map<String, Object> payload;

    @JsonCreator
    public AuditEventDto(
            @JsonProperty("eventId") UUID eventId,
            @JsonProperty("eventType") String eventType,
            @JsonProperty("timestamp") Instant timestamp,
            @JsonProperty("sourceService") String sourceService,
            @JsonProperty("payload") Map<String, Object> payload) {

        this.eventId = Objects.requireNonNull(eventId);
        this.eventType = Objects.requireNonNull(eventType);
        this.timestamp = Objects.requireNonNull(timestamp);
        this.sourceService = Objects.requireNonNull(sourceService);
        this.payload = Objects.requireNonNull(payload);
    }

    public UUID getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public Instant getTimestamp() { return timestamp; }
    public String getSourceService() { return sourceService; }
    public Map<String, Object> getPayload() { return payload; }
}
