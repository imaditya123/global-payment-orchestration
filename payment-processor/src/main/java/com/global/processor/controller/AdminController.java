package com.global.processor.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/processor/admin")
public class AdminController {

    private volatile String mode = "none";   // none | latency | intermittent | permanent
    private volatile int latencyMs = 0;
    private volatile double errorRate = 0.0;

    @PostMapping("/failure-mode")
    public String updateMode(@RequestBody FailureMode req) {
        this.mode = req.mode;
        this.latencyMs = req.latencyMs;
        this.errorRate = req.errorRate;
        return "Updated";
    }

    @GetMapping("/failure-mode")
    public FailureMode getMode() {
        return new FailureMode(this.mode, this.latencyMs, this.errorRate);
    }

    public record FailureMode(String mode, int latencyMs, double errorRate) {}
}
