package com.global.shared.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

public final class EventMetadata {
    private final String correlationId;
    private final Instant timestamp;
    private final String sourceService;
    private final int version;

    @JsonCreator
    public EventMetadata(
            @JsonProperty("correlationId") String correlationId,
            @JsonProperty("timestamp") Instant timestamp,
            @JsonProperty("sourceService") String sourceService,
            @JsonProperty("version") int version) {

        this.correlationId = Objects.requireNonNull(correlationId);
        this.timestamp = Objects.requireNonNull(timestamp);
        this.sourceService = Objects.requireNonNull(sourceService);
        this.version = version;
    }

    public String getCorrelationId() { return correlationId; }
    public Instant getTimestamp() { return timestamp; }
    public String getSourceService() { return sourceService; }
    public int getVersion() { return version; }
}
