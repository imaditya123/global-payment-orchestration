package com.global.settlement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// @Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public class ExecutorIntegrationTests {


    // @Container
    // static PostgreSQLContainer<?> postgres =
    //     new PostgreSQLContainer<>("postgres:15-alpine");

    // @DynamicPropertySource
    // static void props(DynamicPropertyRegistry registry) {
    //     registry.add("spring.datasource.url", postgres::getJdbcUrl);
    //     registry.add("spring.datasource.username", postgres::getUsername);
    //     registry.add("spring.datasource.password", postgres::getPassword);
    // }


    @Test
    void testExecutorHappyPath() {
        // Integration tests using Testcontainers + stubbed PSP endpoints should be added
    }
}
