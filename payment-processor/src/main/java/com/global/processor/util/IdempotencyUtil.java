package com.global.processor.util;

import java.util.UUID;

public class IdempotencyUtil {

    public static String safeUuid() {
        return UUID.randomUUID().toString();
    }

    public static boolean mismatch(Object a, Object b) {
        if (a == null || b == null) return false;
        return !a.equals(b);
    }
}
