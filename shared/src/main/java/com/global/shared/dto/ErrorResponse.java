package com.global.shared.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class ErrorResponse {
    private final String code;
    private final String message;
    private final Instant timestamp;
    private final List<String> details;

    @JsonCreator
    public ErrorResponse(
            @JsonProperty("code") String code,
            @JsonProperty("message") String message,
            @JsonProperty("timestamp") Instant timestamp,
            @JsonProperty("details") List<String> details) {

        this.code = Objects.requireNonNull(code);
        this.message = Objects.requireNonNull(message);
        this.timestamp = Objects.requireNonNull(timestamp);
        this.details = details;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public Instant getTimestamp() { return timestamp; }
    public List<String> getDetails() { return details; }
}
