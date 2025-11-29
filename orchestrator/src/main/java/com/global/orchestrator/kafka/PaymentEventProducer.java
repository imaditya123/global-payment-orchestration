package com.global.orchestrator.kafka;

import com.global.orchestrator.model.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Thin wrapper over KafkaTemplate to publish PaymentEvent objects to payments.events.
 */
@Component
@RequiredArgsConstructor
public class PaymentEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String eventsTopic = "payments.events";

    public void publish(PaymentEvent event) {
        kafkaTemplate.send(eventsTopic, event.getPaymentId().toString(), event);
    }
}
