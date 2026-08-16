package com.anubhab.warrantyhub.dto;

import java.time.LocalDateTime;

public class StatusHistoryResponse {

    private Long historyId;
    private Long requestId;
    private String status;
    private String remarks;
    private String changedBy;
    private LocalDateTime changedAt;

    public StatusHistoryResponse(Long historyId, Long requestId, String status, String remarks,
                                 String changedBy, LocalDateTime changedAt) {
        this.historyId = historyId;
        this.requestId = requestId;
        this.status = status;
        this.remarks = remarks;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public String getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }
}