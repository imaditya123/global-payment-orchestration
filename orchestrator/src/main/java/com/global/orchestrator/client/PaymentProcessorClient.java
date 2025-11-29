package com.global.orchestrator.client;


import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;


@Component
public class PaymentProcessorClient {
    private final RestTemplate rt = new RestTemplate();
    private final String baseUrl;


    public PaymentProcessorClient(@Value("${services.processor.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }


    public ResponseEntity<Map> sendPayment(Map<String, Object> payload) {
        return rt.postForEntity(baseUrl + "/payments/send", new HttpEntity<>(payload), Map.class);
    }
}