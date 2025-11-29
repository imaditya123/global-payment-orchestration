package com.global.orchestrator.saga;

import com.global.orchestrator.model.PaymentEvent;

/**
 * Handler for a single saga step. Implementations may call external services and publish follow-up events.
 */
public interface SagaStepHandler {
    /**
     * Called to handle the provided event for a specific step.
     *
     * @param event incoming payment event
     * @throws Exception if handling fails
     */
    void handle(PaymentEvent event) throws Exception;

    /**
     * Unique step name (e.g. RESERVE_FUNDS, REQUEST_FX)
     */
    String stepName();
}
