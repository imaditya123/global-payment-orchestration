package com.global.apigateway.controller;

import com.global.apigateway.client.FxServiceClient;
import com.global.apigateway.dto.FxConversionResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fx")
public class FxController {

    private final FxServiceClient fxClient;

    public FxController(FxServiceClient fxClient) {
        this.fxClient = fxClient;
    }

    @GetMapping("/convert")
    public Mono<FxConversionResponse> convert(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String amount,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        return fxClient.convert(from, to, amount, correlationId);
    }
}
