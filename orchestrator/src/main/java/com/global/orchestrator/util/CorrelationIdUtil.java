package com.global.orchestrator.util;


import org.slf4j.MDC;


public class CorrelationIdUtil {
    public static final String CORRELATION_ID = "correlationId";


    public static void set(String id) { MDC.put(CORRELATION_ID, id); }
    public static void clear() { MDC.remove(CORRELATION_ID); }
}