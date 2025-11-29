package com.global.processor.dto;

public class GenericResponse {
    private String status;
    private String message;

    public GenericResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
}
