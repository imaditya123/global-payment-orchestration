package com.global.orchestrator.client;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;


import java.util.Map;


@Component
public class AuditClient {
    private final RestTemplate rt = new RestTemplate();
    private final String baseUrl;


    public AuditClient(@Value("${services.audit.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }


    public ResponseEntity<Void> sendAuditEvent(Map<String, Object> payload) {
        return rt.postForEntity(baseUrl + "/audit/event", payload, Void.class);
    }
}