package com.global.orchestrator.saga;

import com.global.orchestrator.entity.PaymentEntity;
import com.global.orchestrator.model.PaymentEvent;
import com.global.orchestrator.model.PaymentStatus;
import com.global.orchestrator.repository.PaymentRepository;
import com.global.orchestrator.service.AuditService;
import com.global.orchestrator.service.PaymentStateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Core orchestrator that routes events to saga steps and manages state transitions.
 */
@Component
@RequiredArgsConstructor
public class PaymentSagaOrchestrator {
    private static final Logger log = LoggerFactory.getLogger(PaymentSagaOrchestrator.class);

    private final PaymentStateService stateService;
    private final AuditService auditService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CompensationHandler compensationHandler;
    private final RetryHandler retryHandler;
    private final PaymentRepository repository;

    private final String commandsTopic = "payments.commands";

    @Transactional
    public void onEvent(PaymentEvent event) {
        log.info("Saga received event type={} payment={}", event.getType(), event.getPaymentId());
        // audit first
        auditService.audit(Map.of(
                "paymentId", event.getPaymentId().toString(),
                "event", event.getType()
        ));

        try {
            switch (event.getType()) {
                case "PAYMENT_CREATED":
                    handlePaymentCreated(event);
                    break;
                case "FUNDS_RESERVED":
                    handleFundsReserved(event);
                    break;
                case "FX_CONVERTED":
                    handleFxConverted(event);
                    break;
                case "PAYMENT_SENT":
                    handlePaymentSent(event);
                    break;
                case "SETTLEMENT_CONFIRMED":
                    handleSettlementConfirmed(event);
                    break;
                default:
                    log.warn("Unhandled event type {} for payment {}", event.getType(), event.getPaymentId());
            }
        } catch (Exception ex) {
            log.error("Saga step failed for payment {} type={} error={}", event.getPaymentId(), event.getType(), ex.getMessage(), ex);
            // Try compensation
            Optional<PaymentEntity> maybe = repository.findById(event.getPaymentId());
            maybe.ifPresent(p -> compensationHandler.compensate(p, ex.getMessage()));
        }
    }

    private void handlePaymentCreated(PaymentEvent event) {
        // Publish RESERVE_FUNDS command
        kafkaTemplate.send(commandsTopic, event.getPaymentId().toString(), Map.of(
                "type", "RESERVE_FUNDS",
                "paymentId", event.getPaymentId().toString(),
                "payload", event.getMetadata()
        ));
        // persist status
        repository.findById(event.getPaymentId()).ifPresent(p -> stateService.updateStatus(p, PaymentStatus.PENDING));
    }

    private void handleFundsReserved(PaymentEvent event) {
        // Proceed to request FX
        kafkaTemplate.send(commandsTopic, event.getPaymentId().toString(), Map.of(
                "type", "REQUEST_FX",
                "paymentId", event.getPaymentId().toString(),
                "payload", event.getMetadata()
        ));
        repository.findById(event.getPaymentId()).ifPresent(p -> stateService.updateStatus(p, PaymentStatus.RESERVED));
    }

    private void handleFxConverted(PaymentEvent event) {
        // Send payment with converted amount
        kafkaTemplate.send(commandsTopic, event.getPaymentId().toString(), Map.of(
                "type", "SEND_PAYMENT",
                "paymentId", event.getPaymentId().toString(),
                "payload", event.getMetadata()
        ));
        repository.findById(event.getPaymentId()).ifPresent(p -> stateService.updateStatus(p, PaymentStatus.FX_CONVERTED));
    }

    private void handlePaymentSent(PaymentEvent event) {
        repository.findById(event.getPaymentId()).ifPresent(p -> stateService.updateStatus(p, PaymentStatus.SENT));
        // publish settlement batch request
        kafkaTemplate.send("settlement.batch", event.getPaymentId().toString(), Map.of("paymentId", event.getPaymentId().toString()));
    }

    private void handleSettlementConfirmed(PaymentEvent event) {
        repository.findById(event.getPaymentId()).ifPresent(p -> {
            stateService.updateStatus(p, PaymentStatus.SETTLED);
            stateService.updateStatus(p, PaymentStatus.COMPLETED);
        });
    }

    /**
     * Called by the RetryTopicListener to execute a retry attempt; simplified: republish the command for the step.
     */
    public void retryStep(UUID paymentId, String step, int retryCount) {
        log.info("Retrying step={} for payment={} attempt={}", step, paymentId, retryCount);
        kafkaTemplate.send(commandsTopic, paymentId.toString(), Map.of(
                "type", step,
                "paymentId", paymentId.toString(),
                "retryCount", retryCount
        ));
    }
}
