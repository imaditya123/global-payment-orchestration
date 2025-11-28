package com.global.apigateway.client;

import com.global.apigateway.dto.PaymentRequest;
import com.global.apigateway.dto.PaymentResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OrchestratorClient {

    private final WebClient webClient;

    public OrchestratorClient(@Qualifier("orchestratorWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<PaymentResponse> createPayment(PaymentRequest request, String correlationId) {
        return webClient.post()
                .uri("/api/internal/payments")
                .header("X-Correlation-Id", correlationId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PaymentResponse.class);
    }

    public Mono<PaymentResponse> getPaymentStatus(String id, String correlationId) {
        return webClient.get()
                .uri("/api/internal/payments/{id}", id)
                .header("X-Correlation-Id", correlationId)
                .retrieve()
                .bodyToMono(PaymentResponse.class);
    }
}
