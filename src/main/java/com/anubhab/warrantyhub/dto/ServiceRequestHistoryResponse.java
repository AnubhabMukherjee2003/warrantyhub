package com.anubhab.warrantyhub.dto;

import java.util.List;

public class ServiceRequestHistoryResponse {

    private ServiceRequestResponse serviceRequest;
    private List<StatusHistoryResponse> history;

    public ServiceRequestHistoryResponse(ServiceRequestResponse serviceRequest, List<StatusHistoryResponse> history) {
        this.serviceRequest = serviceRequest;
        this.history = history;
    }

    public ServiceRequestResponse getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(ServiceRequestResponse serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    public List<StatusHistoryResponse> getHistory() {
        return history;
    }

    public void setHistory(List<StatusHistoryResponse> history) {
        this.history = history;
    }
}
