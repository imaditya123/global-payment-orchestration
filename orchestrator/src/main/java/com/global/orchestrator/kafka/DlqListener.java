package com.global.orchestrator.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Very simple DLQ listener to log and potentially persist dead-lettered messages.
 */
@Component
@RequiredArgsConstructor
public class DlqListener {
    private static final Logger log = LoggerFactory.getLogger(DlqListener.class);

    @KafkaListener(topics = "payments.dlq", groupId = "orchestrator-dlq", containerFactory = "kafkaListenerContainerFactory")
    public void onDlq(Object msg) {
        log.error("DLQ message received: {}", msg);
        // TODO: persist to DB / alerting
    }
}
