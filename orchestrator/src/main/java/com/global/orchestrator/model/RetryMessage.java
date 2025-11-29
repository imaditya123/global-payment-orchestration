package com.global.orchestrator.model;


import lombok.*;


import java.time.OffsetDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetryMessage {
    private UUID paymentId;
    private String step;
    private int retryCount;
    private OffsetDateTime nextAttemptAt;
}