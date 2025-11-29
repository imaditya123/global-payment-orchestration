package com.global.processor.dto;

import java.math.BigDecimal;

public class ReserveResponse {

    private String status;
    private String reservationId;
    private BigDecimal reservedAmount;
    private String currency;

    public ReserveResponse(String status, String reservationId, BigDecimal reservedAmount, String currency) {
        this.status = status;
        this.reservationId = reservationId;
        this.reservedAmount = reservedAmount;
        this.currency = currency;
    }

    public String getStatus() { return status; }
    public String getReservationId() { return reservationId; }
    public BigDecimal getReservedAmount() { return reservedAmount; }
    public String getCurrency() { return currency; }
}
