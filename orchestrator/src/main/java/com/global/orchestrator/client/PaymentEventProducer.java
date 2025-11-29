package com.global.orchestrator.client;

import com.global.orchestrator.model.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Produces payment events to the Kafka topic: payments.events
 */
@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.events:payments.events}")
    private String eventsTopic;

    /**
     * Publish a PaymentEvent to Kafka.
     */
    public void publish(PaymentEvent event) {
        try {
            log.info("Publishing event type={} paymentId={} to topic={}",
                    event.getType(), event.getPaymentId(), eventsTopic);

            kafkaTemplate.send(eventsTopic, event.getPaymentId().toString(), event);
        } catch (Exception ex) {
            log.error("Failed to publish PaymentEvent", ex);
            throw ex; // Optional: wrap in custom exception
        }
    }
}
