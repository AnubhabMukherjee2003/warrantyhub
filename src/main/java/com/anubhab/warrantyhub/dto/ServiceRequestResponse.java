package com.anubhab.warrantyhub.dto;

import java.time.LocalDateTime;

public class ServiceRequestResponse {

    private Long requestId;
    private Long purchaseId;
    private String issueCategory;
    private String issueDescription;
    private String priority;
    private String currentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ServiceRequestResponse(Long requestId, Long purchaseId, String issueCategory,
                                  String issueDescription, String priority, String currentStatus,
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.requestId = requestId;
        this.purchaseId = purchaseId;
        this.issueCategory = issueCategory;
        this.issueDescription = issueDescription;
        this.priority = priority;
        this.currentStatus = currentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getRequestId() {
        return requestId;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public String getIssueCategory() {
        return issueCategory;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public String getPriority() {
        return priority;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}