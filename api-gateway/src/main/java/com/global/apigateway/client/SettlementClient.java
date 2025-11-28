package com.global.apigateway.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class SettlementClient {

    private final WebClient webClient;

    public SettlementClient(@Qualifier("settlementWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> runSettlement(String correlationId) {
        return webClient.post()
                .uri("/api/internal/settlement/run")
                .header("X-Correlation-Id", correlationId)
                .retrieve()
                .bodyToMono(String.class);
    }
}
