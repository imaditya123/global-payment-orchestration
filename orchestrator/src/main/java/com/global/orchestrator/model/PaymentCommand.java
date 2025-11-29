package com.global.orchestrator.model;


import lombok.*;


import java.util.Map;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCommand {
    private UUID paymentId;
    private String type; // e.g. RESERVE_FUNDS, SEND_PAYMENT
    private Map<String, Object> payload;
}