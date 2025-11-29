package com.global.audit.kafka;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.audit.model.EventType;
import com.global.audit.service.AuditIngestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuditEventListener {
    private static final Logger log = LoggerFactory.getLogger(AuditEventListener.class);


    private final ObjectMapper mapper = new ObjectMapper();
    private final AuditIngestService ingestService;


    public AuditEventListener(AuditIngestService ingestService) {
        this.ingestService = ingestService;
    }


    @KafkaListener(topics = "payments.events", groupId = "${kafka.consumer.group-id}")
    public void handle(String message) {
        try {
            JsonNode node = mapper.readTree(message);
            // Basic defensive parsing — map fields expected in the internal event model
            String eventTypeStr = node.has("eventType") ? node.get("eventType").asText() : "UNKNOWN";
            EventType eventType;
            try { eventType = EventType.valueOf(eventTypeStr); } catch (Exception e) { eventType = EventType.UNKNOWN; }


            ingestService.ingestRaw(message, eventType);
        } catch (Exception e) {
            log.error("Failed to parse message: {}", e.getMessage(), e);
        }
    }
}