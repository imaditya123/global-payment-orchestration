package com.global.apigateway.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuditServiceClient {

    private final WebClient webClient;

    public AuditServiceClient(@Qualifier("auditWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getPaymentTimeline(String id, String correlationId) {
        return webClient.get()
                .uri("/api/internal/audit/payment/{id}", id)
                .header("X-Correlation-Id", correlationId)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> searchByVendor(String vendorId, String correlationId) {
        return webClient.get()
                .uri("/api/internal/audit/vendor/{vendorId}", vendorId)
                .header("X-Correlation-Id", correlationId)
                .retrieve()
                .bodyToMono(String.class);
    }
}
