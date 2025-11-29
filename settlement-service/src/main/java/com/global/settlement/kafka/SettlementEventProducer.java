package com.global.settlement.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SettlementEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic = "settlement-events";

    public SettlementEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String key, String eventJson) {
        kafkaTemplate.send(topic, key, eventJson);
    }
}
