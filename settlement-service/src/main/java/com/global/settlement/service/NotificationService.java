package com.global.settlement.service;

import org.slf4j.*;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void notifyOps(String severity, String message) {
        // Hook into alerting: Slack, email, PagerDuty
        log.warn("ALERT [{}]: {}", severity, message);
    }
}
