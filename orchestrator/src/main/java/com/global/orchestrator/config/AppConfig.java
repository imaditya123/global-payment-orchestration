package com.global.orchestrator.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {


    @Value("${orchestration.retry.max-attempts:5}")
    private int maxAttempts;


    @Value("${orchestration.retry.backoff-ms:2000}")
    private long backoffMs;


    public int getMaxAttempts() { return maxAttempts; }
    public long getBackoffMs() { return backoffMs; }
}