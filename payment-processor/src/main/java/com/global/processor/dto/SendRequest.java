package com.global.processor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public class SendRequest {

    @NotBlank
    private String paymentId;

    private String reservationId;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String currency;

    private Map<String, String> destination;

    // Getters & Setters
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Map<String, String> getDestination() { return destination; }
    public void setDestination(Map<String, String> destination) { this.destination = destination; }
}
