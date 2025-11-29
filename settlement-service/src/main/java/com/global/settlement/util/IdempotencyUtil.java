package com.global.settlement.util;

import java.util.UUID;

public final class IdempotencyUtil {

    private IdempotencyUtil() {}

    public static String batchReferenceForWindow(String currency, String psp, String windowIso) {
        // deterministic idempotency key for a given grouping + window
        return currency + "-" + psp + "-" + windowIso;
    }

    public static String randomId() {
        return UUID.randomUUID().toString();
    }
}
