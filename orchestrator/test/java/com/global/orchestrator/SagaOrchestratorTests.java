package com.global.orchestrator;

import com.global.orchestrator.entity.PaymentEntity;
import com.global.orchestrator.model.PaymentEvent;
import com.global.orchestrator.repository.PaymentRepository;
import com.global.orchestrator.saga.PaymentSagaOrchestrator;
import com.global.orchestrator.service.AuditService;
import com.global.orchestrator.service.PaymentStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class SagaOrchestratorTests {

    @Mock private PaymentStateService stateService;
    @Mock private AuditService auditService;
    @Mock private KafkaTemplate<String, Object> kafkaTemplate;
    @Mock private com.global.orchestrator.saga.CompensationHandler compensationHandler;
    @Mock private com.global.orchestrator.saga.RetryHandler retryHandler;
    @Mock private PaymentRepository repository;

    @InjectMocks private PaymentSagaOrchestrator orchestrator;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenPaymentCreated_thenReserveFundsCommandPublishedAndStatePending() {
        UUID pid = UUID.randomUUID();
        PaymentEntity entity = PaymentEntity.builder()
                .id(pid)
                .vendorId("vendorA")
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .build();

        when(repository.findById(pid)).thenReturn(Optional.of(entity));

        PaymentEvent event = PaymentEvent.builder()
                .paymentId(pid)
                .type("PAYMENT_CREATED")
                .metadata(Map.of("amount", "100.00"))
                .build();

        orchestrator.onEvent(event);

        verify(kafkaTemplate, times(1)).send(eq("payments.commands"), eq(pid.toString()), any());
        verify(stateService, times(1)).updateStatus(entity, com.global.orchestrator.model.PaymentStatus.PENDING);
        verify(auditService, times(1)).audit(any());
    }

    @Test
    void whenFundsReserved_thenRequestFxPublishedAndStatusUpdated() {
        UUID pid = UUID.randomUUID();
        PaymentEntity e = PaymentEntity.builder().id(pid).build();
        when(repository.findById(pid)).thenReturn(Optional.of(e));

        PaymentEvent event = PaymentEvent.builder().paymentId(pid).type("FUNDS_RESERVED").metadata(Map.of()).build();
        orchestrator.onEvent(event);

        verify(kafkaTemplate).send(eq("payments.commands"), eq(pid.toString()), any());
        verify(stateService).updateStatus(e, com.global.orchestrator.model.PaymentStatus.RESERVED);
    }
}
