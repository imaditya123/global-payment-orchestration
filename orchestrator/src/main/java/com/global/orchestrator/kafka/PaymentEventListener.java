package com.global.orchestrator.kafka;

import com.global.orchestrator.model.PaymentEvent;
import com.global.orchestrator.saga.PaymentSagaOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Listens on payments.events and forwards to the orchestrator.
 */
@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private final PaymentSagaOrchestrator orchestrator;

    @KafkaListener(topics = "payments.events", groupId = "orchestrator-group", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(PaymentEvent event) {
        orchestrator.onEvent(event);
    }
}
