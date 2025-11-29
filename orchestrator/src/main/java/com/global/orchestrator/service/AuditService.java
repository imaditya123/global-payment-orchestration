package com.global.orchestrator.service;

import com.global.orchestrator.client.AuditClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditService {
    private static final Logger log = LoggerFactory.getLogger(AuditService.class);
    private final AuditClient auditClient;

    /**
     * Best-effort synchronous audit call. Exceptions are swallowed but logged to avoid blocking saga flow.
     */
    public void audit(Map<String, Object> event) {
        try {
            auditClient.sendAuditEvent(event);
        } catch (Exception ex) {
            log.warn("Audit service failed: {}", ex.getMessage(), ex);
        }
    }
}
