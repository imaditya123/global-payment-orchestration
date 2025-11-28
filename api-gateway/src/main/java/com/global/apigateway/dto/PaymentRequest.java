package com.global.apigateway.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String vendorId;

    private Map<String, String> metadata;
}
