package com.global.apigateway.controller;

import com.global.apigateway.client.AuditServiceClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {

    private final AuditServiceClient auditClient;

    public AuditController(AuditServiceClient auditClient) {
        this.auditClient = auditClient;
    }

    @GetMapping("/payment/{id}")
    public Mono<String> getPaymentTimeline(
            @PathVariable String id,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        return auditClient.getPaymentTimeline(id, correlationId);
    }

    @GetMapping("/vendor/{vendorId}")
    public Mono<String> searchByVendor(
            @PathVariable String vendorId,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        return auditClient.searchByVendor(vendorId, correlationId);
    }
}
