package com.global.orchestrator;

import com.global.orchestrator.model.RetryMessage;
import com.global.orchestrator.service.RetryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.kafka.core.KafkaTemplate;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

class RetryTests {

    @Mock private KafkaTemplate<String, Object> kafkaTemplate;
    @InjectMocks private RetryService retryService;

    @BeforeEach
    void setup() { MockitoAnnotations.openMocks(this); }

    @Test
    void scheduleRetry_sendsToTopic() {
        UUID id = UUID.randomUUID();
        RetryMessage msg = RetryMessage.builder()
                .paymentId(id)
                .step("RESERVE_FUNDS")
                .retryCount(1)
                .nextAttemptAt(OffsetDateTime.now())
                .build();

        retryService.scheduleRetry("payments.retries", msg);

        verify(kafkaTemplate, times(1)).send(eq("payments.retries"), eq(id.toString()), eq(msg));
    }
}
