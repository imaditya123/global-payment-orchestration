package com.global.orchestrator.service;

import com.global.orchestrator.entity.PaymentEntity;
import com.global.orchestrator.model.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Executes compensation steps:
 *  - release reserved funds
 *  - mark payment FAILED
 *  - audit compensation
 */
@Service
@RequiredArgsConstructor
public class CompensationService {
    private static final Logger log = LoggerFactory.getLogger(CompensationService.class);

    private final PaymentStateService stateService;
    private final AuditService auditService;

    public void compensate(PaymentEntity payment, String reason) {
        log.info("Compensating payment {} reason={}", payment.getId(), reason);

        // TODO: call payment processor to release funds if necessary (best-effort/async)
        // For now just mark state and audit.
        stateService.updateStatus(payment, PaymentStatus.FAILED);

        auditService.audit(java.util.Map.of(
                "paymentId", payment.getId().toString(),
                "action", "COMPENSATE",
                "reason", reason
        ));
    }
}
