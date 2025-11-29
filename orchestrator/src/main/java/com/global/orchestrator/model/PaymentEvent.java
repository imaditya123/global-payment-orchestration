package com.global.orchestrator.model;


import lombok.*;


import java.util.Map;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEvent {

    private UUID paymentId;
    private String type; // e.g. PAYMENT_CREATED, FUNDS_RESERVED
    private Map<String, Object> metadata;
    
}