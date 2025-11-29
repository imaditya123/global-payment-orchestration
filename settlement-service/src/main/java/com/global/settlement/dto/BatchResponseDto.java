package com.global.settlement.dto;

import java.util.UUID;

public class BatchResponseDto {
    private UUID batchId;
    private String status;

    public BatchResponseDto(UUID batchId, String status) {
        this.batchId = batchId;
        this.status = status;
    }

    public UUID getBatchId() { return batchId; }
    public String getStatus() { return status; }
}
