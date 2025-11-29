package com.global.orchestrator.saga;

import com.global.orchestrator.entity.PaymentEntity;
import com.global.orchestrator.service.CompensationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adapter to trigger compensation flows for a given payment.
 */
@Component
@RequiredArgsConstructor
public class CompensationHandler {
    private final CompensationService compensationService;

    public void compensate(PaymentEntity payment, String reason) {
        compensationService.compensate(payment, reason);
    }
}
