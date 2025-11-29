package com.global.audit.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.audit.entity.AuditEventEntity;
import com.global.audit.model.EventType;
import com.global.audit.repository.AuditEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.UUID;


@Service
public class AuditIngestService {
    private static final Logger log = LoggerFactory.getLogger(AuditIngestService.class);


    private final AuditEventRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();


    public AuditIngestService(AuditEventRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public void ingestRaw(String rawJson, EventType eventType) {
        try {
            // keep raw JSON in payload; also try to extract known metadata
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(rawJson);


            AuditEventEntity entity = new AuditEventEntity();


            if (root.has("paymentId") && !root.get("paymentId").isNull()) {
            try { entity.setPaymentId(UUID.fromString(root.get("paymentId").asText())); } catch (Exception ignored) {}
            }


            if (root.has("vendorId")) entity.setVendorId(root.get("vendorId").asText(null));
            if (root.has("correlationId")) entity.setCorrelationId(root.get("correlationId").asText(null));


            entity.setEventType(eventType.name());
            entity.setPayload(rawJson);
            entity.setCreatedAt(root.has("timestamp") ? Instant.parse(root.get("timestamp").asText()) : Instant.now());


        repository.save(entity);
        } catch (Exception e) {
            log.error("Failed to ingest raw audit event: {}", e.getMessage(), e);
        }
    }
}