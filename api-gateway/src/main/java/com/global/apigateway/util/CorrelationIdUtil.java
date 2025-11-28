package com.global.apigateway.util;


import java.util.UUID;


public final class CorrelationIdUtil {
    private CorrelationIdUtil() {}


    public static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}