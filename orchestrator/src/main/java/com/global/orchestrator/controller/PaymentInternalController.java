package com.global.orchestrator.controller;

import com.global.orchestrator.client.PaymentEventProducer;
import com.global.orchestrator.entity.PaymentEntity;
import com.global.orchestrator.model.PaymentEvent;
import com.global.orchestrator.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/internal/payments")
@RequiredArgsConstructor
public class PaymentInternalController {

    private final PaymentRepository repository;
    private final PaymentEventProducer eventProducer;

    /**
     * Create payment record (internal). Produces PAYMENT_CREATED event.
     */
    @PostMapping("/create")
    public ResponseEntity<PaymentEntity> create(@RequestBody PaymentEntity payload) {
        if (payload.getId() == null) payload.setId(UUID.randomUUID());
        var saved = repository.save(payload);

        // publish PAYMENT_CREATED event to kick off saga
        var event = com.global.orchestrator.model.PaymentEvent.builder()
                .paymentId(saved.getId())
                .type("PAYMENT_CREATED")
                .metadata(java.util.Map.of(
                        "vendorId", saved.getVendorId(),
                        "amount", saved.getAmount(),
                        "currency", saved.getCurrency(),
                        "targetCurrency", saved.getTargetCurrency()
                ))
                .build();
        eventProducer.publish(event);

        return ResponseEntity.ok(saved);
    }

    /**
     * Internal endpoint to post arbitrary payment events (used by test harness or other internal services).
     */
    @PostMapping("/event")
    public ResponseEntity<Void> postEvent(@RequestBody PaymentEvent event) {
        eventProducer.publish(event);
        return ResponseEntity.accepted().build();
    }
}
