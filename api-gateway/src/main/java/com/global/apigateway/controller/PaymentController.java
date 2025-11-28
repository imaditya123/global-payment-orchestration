package com.global.apigateway.controller;

import com.global.apigateway.dto.PaymentRequest;
import com.global.apigateway.dto.PaymentResponse;
import com.global.apigateway.service.PaymentGatewayService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class PaymentController {

    private final PaymentGatewayService gatewayService;

    public PaymentController(PaymentGatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @PostMapping
    public Mono<ResponseEntity<PaymentResponse>> createPayment(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        String corr = correlationId;

        return gatewayService.createPayment(request, corr)
                .map(resp -> ResponseEntity.accepted()
                        .header("X-Correlation-Id", corr)
                        .body(resp));
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<PaymentResponse>> getPaymentStatus(
            @PathVariable String id,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        String corr = correlationId;

        return gatewayService.getPaymentStatus(id, corr)
                .map(resp -> ResponseEntity.ok()
                        .header("X-Correlation-Id", corr)
                        .body(resp));
    }

}
