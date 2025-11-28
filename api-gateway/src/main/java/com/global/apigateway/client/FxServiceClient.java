package com.global.apigateway.client;

import com.global.apigateway.dto.FxConversionResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class FxServiceClient {

    private final WebClient webClient;

    public FxServiceClient(@Qualifier("fxWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<FxConversionResponse> convert(String from, String to, String amount, String correlationId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/internal/fx/convert")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .queryParam("amount", amount)
                        .build())
                .header("X-Correlation-Id", correlationId)
                .retrieve()
                .bodyToMono(FxConversionResponse.class);
    }
}
