package com.global.orchestrator.service;

import com.global.orchestrator.model.RetryMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Publishes retry messages to the retries topic. In production this would support scheduling and persistence.
 */
@Service
@RequiredArgsConstructor
public class RetryService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void scheduleRetry(String topic, RetryMessage message) {
        // For simplicity we publish immediately. A production design would persist and use a scheduler or broker-delayed message.
        kafkaTemplate.send(topic, message.getPaymentId().toString(), message);
    }
}
