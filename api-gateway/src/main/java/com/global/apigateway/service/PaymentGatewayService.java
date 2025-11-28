package com.global.apigateway.service;

import com.global.apigateway.client.OrchestratorClient;
import com.global.apigateway.dto.PaymentRequest;
import com.global.apigateway.dto.PaymentResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentGatewayService {

    private final OrchestratorClient orchestratorClient;

    public PaymentGatewayService(OrchestratorClient orchestratorClient) {
        this.orchestratorClient = orchestratorClient;
    }

    public Mono<PaymentResponse> createPayment(PaymentRequest request, String correlationId) {
        return orchestratorClient.createPayment(request, correlationId);
    }

    public Mono<PaymentResponse> getPaymentStatus(String id, String correlationId) {
        return orchestratorClient.getPaymentStatus(id, correlationId);
    }
}
