package com.global.processor.model;

public class PaymentResult {

    private boolean success;
    private String transactionId;
    private String errorMessage;

    public PaymentResult(boolean success, String transactionId, String errorMessage) {
        this.success = success;
        this.transactionId = transactionId;
        this.errorMessage = errorMessage;
    }

    public static PaymentResult ok(String txId) {
        return new PaymentResult(true, txId, null);
    }

    public static PaymentResult failed(String reason) {
        return new PaymentResult(false, null, reason);
    }

    public boolean isSuccess() { return success; }
    public String getTransactionId() { return transactionId; }
    public String getErrorMessage() { return errorMessage; }
}
