package com.global.processor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public String correlationIdHeader() {
        return "X-Correlation-Id";
    }
}
