package com.global.shared.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.global.shared.enums.Currency;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public final class PaymentRequest {
    private final UUID paymentId;
    private final BigDecimal amount;
    private final Currency currency;
    private final String beneficiaryAccount;
    private final String sourceAccount;

    @JsonCreator
    public PaymentRequest(
            @JsonProperty("paymentId") UUID paymentId,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("currency") Currency currency,
            @JsonProperty("beneficiaryAccount") String beneficiaryAccount,
            @JsonProperty("sourceAccount") String sourceAccount) {

        this.paymentId = Objects.requireNonNull(paymentId);
        this.amount = Objects.requireNonNull(amount);
        this.currency = Objects.requireNonNull(currency);
        this.beneficiaryAccount = Objects.requireNonNull(beneficiaryAccount);
        this.sourceAccount = Objects.requireNonNull(sourceAccount);
    }

    public UUID getPaymentId() { return paymentId; }
    public BigDecimal getAmount() { return amount; }
    public Currency getCurrency() { return currency; }
    public String getBeneficiaryAccount() { return beneficiaryAccount; }
    public String getSourceAccount() { return sourceAccount; }
}
