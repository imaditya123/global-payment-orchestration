package com.global.shared.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

public final class SagaEventHeader {
    private final String sagaId;
    private final Map<String, String> data;

    @JsonCreator
    public SagaEventHeader(
            @JsonProperty("sagaId") String sagaId,
            @JsonProperty("data") Map<String, String> data) {

        this.sagaId = Objects.requireNonNull(sagaId);
        this.data = Objects.requireNonNull(data);
    }

    public String getSagaId() { return sagaId; }
    public Map<String, String> getData() { return data; }
}
