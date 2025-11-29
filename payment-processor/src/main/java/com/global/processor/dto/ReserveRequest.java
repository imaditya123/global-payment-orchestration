package com.global.processor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

public class ReserveRequest {

    @NotBlank
    private String paymentId;

    @NotBlank
    private String vendorId;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String currency;

    private Map<String, Object> metadata;

    // Getters & Setters
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getVendorId() { return vendorId; }
    public void setVendorId(String vendorId) { this.vendorId = vendorId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
