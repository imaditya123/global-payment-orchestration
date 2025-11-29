package com.global.processor.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String topic, String key, String payload) {
        kafkaTemplate.send(topic, key, payload);
    }

    // Helper methods
    public void fundsReserved(String paymentId, String reservationId) {
        publish("payments.events", paymentId,
                "{\"type\":\"FundsReserved\",\"paymentId\":\"" + paymentId +
                "\",\"reservationId\":\"" + reservationId + "\"}");
    }

    public void paymentSent(String paymentId, String txId) {
        publish("payments.events", paymentId,
                "{\"type\":\"PaymentSent\",\"paymentId\":\"" + paymentId +
                "\",\"transactionId\":\"" + txId + "\"}");
    }

    public void paymentFailed(String paymentId, String reason) {
        publish("payments.events", paymentId,
                "{\"type\":\"PaymentFailed\",\"paymentId\":\"" + paymentId +
                "\",\"reason\":\"" + reason + "\"}");
    }

    public void reservationReleased(String paymentId, String reservationId) {
        publish("payments.events", paymentId,
                "{\"type\":\"ReservationReleased\",\"paymentId\":\"" + paymentId +
                "\",\"reservationId\":\"" + reservationId + "\"}");
    }
}
