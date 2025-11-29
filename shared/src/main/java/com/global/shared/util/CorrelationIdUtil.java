package com.global.shared.util;

import com.global.shared.constants.HeaderKeys;

import java.util.Optional;
import java.util.UUID;

public final class CorrelationIdUtil {

    private CorrelationIdUtil() { }

    public static String create() {
        return UUID.randomUUID().toString();
    }

    public static Optional<String> fromHeaders(java.util.Map<String, ? extends Object> headers) {
        if (headers == null) return Optional.empty();
        Object val = headers.get(HeaderKeys.X_CORRELATION_ID);
        if (val == null) return Optional.empty();
        return Optional.of(val.toString());
    }

    public static void putToHeaders(java.util.Map<String, Object> headers, String correlationId) {
        headers.put(HeaderKeys.X_CORRELATION_ID, correlationId);
    }
}
