package com.global.apigateway.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String paymentId;
    private String status;
    private String message;
}
