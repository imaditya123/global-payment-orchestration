package com.global.orchestrator.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class KafkaConfig {


    @Value("${kafka.topics.events:payments.events}")
    private String eventsTopic;


    @Value("${kafka.topics.commands:payments.commands}")
    private String commandsTopic;


    @Value("${kafka.topics.retries:payments.retries}")
    private String retriesTopic;


    @Value("${kafka.topics.dlq:payments.dlq}")
    private String dlqTopic;


    @Bean
    public NewTopic eventsTopic() {
        return new NewTopic(eventsTopic, 3, (short)1);
    }


    @Bean
    public NewTopic commandsTopic() {
        return new NewTopic(commandsTopic, 3, (short)1);
    }


    @Bean
    public NewTopic retriesTopic() {
        return new NewTopic(retriesTopic, 1, (short)1);
    }


    @Bean
    public NewTopic dlqTopic() {
        return new NewTopic(dlqTopic, 1, (short)1);
    }
}