package com.global.apigateway.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {


    @Value("${services.orchestrator.base-url}")
    private String orchestratorBaseUrl;


    @Value("${services.audit.base-url}")
    private String auditBaseUrl;


    @Value("${services.fx.base-url}")
    private String fxBaseUrl;


    @Value("${services.settlement.base-url}")
    private String settlementBaseUrl;


    @Bean("orchestratorWebClient")
    public WebClient orchestratorWebClient(WebClient.Builder builder) {
        return builder.baseUrl(orchestratorBaseUrl)
                .build();
    }


    @Bean("auditWebClient")
    public WebClient auditWebClient(WebClient.Builder builder) {
        return builder.baseUrl(auditBaseUrl).build();
    }


    @Bean("fxWebClient")
    public WebClient fxWebClient(WebClient.Builder builder) {
        return builder.baseUrl(fxBaseUrl).build();
    }


    @Bean("settlementWebClient")
    public WebClient settlementWebClient(WebClient.Builder builder) {
        return builder.baseUrl(settlementBaseUrl).build();
    }
}