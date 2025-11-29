package com.global.orchestrator.kafka;

import com.global.orchestrator.model.RetryMessage;
import com.global.orchestrator.saga.PaymentSagaOrchestrator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * Consumes retry topic messages and triggers orchestrator retry.
 */
@Component
@RequiredArgsConstructor
public class RetryTopicListener {
    private static final Logger log = LoggerFactory.getLogger(RetryTopicListener.class);
    private final PaymentSagaOrchestrator orchestrator;

    @KafkaListener(topics = "payments.retries", groupId = "orchestrator-retry", containerFactory = "kafkaListenerContainerFactory")
    public void onRetry(RetryMessage msg) {
        log.info("Received retry message payment={} step={} retryCount={} nextAt={}",
                msg.getPaymentId(), msg.getStep(), msg.getRetryCount(), msg.getNextAttemptAt());

        // Only process if nextAttemptAt <= now (simple scheduling guard)
        if (msg.getNextAttemptAt() == null || !msg.getNextAttemptAt().isAfter(OffsetDateTime.now())) {
            orchestrator.retryStep(msg.getPaymentId(), msg.getStep(), msg.getRetryCount());
        } else {
            log.info("Skipping retry for payment={} until nextAttemptAt={}", msg.getPaymentId(), msg.getNextAttemptAt());
            // In production re-publish or schedule accordingly
        }
    }
}
