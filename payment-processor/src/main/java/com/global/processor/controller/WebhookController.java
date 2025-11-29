package com.global.processor.controller;

import com.global.processor.service.WebhookProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/processor/webhook")
public class WebhookController {

    private final WebhookProcessingService service;

    public WebhookController(WebhookProcessingService service) {
        this.service = service;
    }

    @PostMapping("/callback")
    public ResponseEntity<?> callback(@RequestBody WebhookPayload payload) {
        service.handleAsyncStatus(payload.paymentId, payload.status);
        return ResponseEntity.ok().build();
    }

    public static class WebhookPayload {
        public String paymentId;
        public String status;
    }
}
