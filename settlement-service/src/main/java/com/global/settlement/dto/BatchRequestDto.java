package com.global.settlement.dto;

import java.time.OffsetDateTime;

public class BatchRequestDto {
    private OffsetDateTime window;
    private String currency;
    private String psp;

    // getters setters
    public OffsetDateTime getWindow() { return window; }
    public void setWindow(OffsetDateTime window) { this.window = window; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getPsp() { return psp; }
    public void setPsp(String psp) { this.psp = psp; }
}
