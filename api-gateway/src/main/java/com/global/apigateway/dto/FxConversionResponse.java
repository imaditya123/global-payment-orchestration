package com.global.apigateway.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FxConversionResponse {
    private BigDecimal sourceAmount;
    private String sourceCurrency;
    private BigDecimal targetAmount;
    private String targetCurrency;
    private BigDecimal rate;
}
