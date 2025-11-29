package com.global.shared.constants;

public final class KafkaTopics {

    private KafkaTopics() { }

    public static final String PAYMENT_EVENTS = "payments.events";
    public static final String PAYMENT_RETRY  = "payments.retry";
    public static final String PAYMENT_DLQ    = "payments.dlq";

    public static final String SETTLEMENT_EVENTS = "settlement.events";
}
