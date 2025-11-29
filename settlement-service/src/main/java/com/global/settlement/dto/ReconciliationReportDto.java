package com.global.settlement.dto;

import java.util.List;

public class ReconciliationReportDto {
    private String date;
    private List<String> issues;

    public ReconciliationReportDto(String date, List<String> issues) {
        this.date = date;
        this.issues = issues;
    }

    public String getDate() { return date; }
    public List<String> getIssues() { return issues; }
}
