package com.global.apigateway.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public byte[] toJsonBytes() {
        try {
            return new ObjectMapper().writeValueAsBytes(this);
        } catch (Exception ex) {
            return ("{\"message\":\"" + ex.getMessage() + "\"}").getBytes();
        }
    }
}
