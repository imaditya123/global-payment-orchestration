package com.global.orchestrator.saga;

import com.global.orchestrator.model.RetryMessage;
import com.global.orchestrator.service.RetryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * Centralizes retry/backoff decision logic.
 */
@Component
@RequiredArgsConstructor
public class RetryHandler {
    private static final Logger log = LoggerFactory.getLogger(RetryHandler.class);
    private final RetryService retryService;
    private final com.global.orchestrator.config.AppConfig appConfig;

    /**
     * Request a retry for a step. Will compute nextAttemptAt using exponential backoff and schedule via RetryService.
     */
    public void scheduleRetry(java.util.UUID paymentId, String step, int currentRetryCount) {
        int nextRetry = currentRetryCount + 1;
        long baseBackoff = appConfig.getBackoffMs();
        long nextDelay = computeBackoffMillis(baseBackoff, nextRetry);

        var nextAt = OffsetDateTime.now().plusNanos(nextDelay * 1_000_000L);

        var msg = RetryMessage.builder()
                .paymentId(paymentId)
                .step(step)
                .retryCount(nextRetry)
                .nextAttemptAt(nextAt)
                .build();

        log.info("Scheduling retry for payment={} step={} attempt={} nextAt={}", paymentId, step, nextRetry, nextAt);
        retryService.scheduleRetry("payments.retries", msg);
    }

    private long computeBackoffMillis(long baseMs, int attempt) {
        // Exponential backoff with jitter
        long expo = baseMs * (1L << Math.min(attempt - 1, 10));
        long jitter = (long) (Math.random() * baseMs);
        return expo + jitter;
    }
}
