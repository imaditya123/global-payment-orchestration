package com.global.settlement.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Placeholder - listens for confirmations or partner messages if needed.
 */
@Component
public class SettlementEventListener {

    @KafkaListener(topics = "psp-confirmations", groupId = "settlement-service")
    public void onConfirmation(String message) {
        // parse confirmation and forward to ConfirmationHandler (autowired)
        // For brevity, implementation omitted; actual implementation should parse JSON and call ConfirmationHandler.
    }
}
