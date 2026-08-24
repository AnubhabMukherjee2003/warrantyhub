package com.anubhab.warrantyhub.dto;

import java.util.List;

public class ServiceRequestHistoryResponse {
    private ProductResponse product;
    private WarrantyResponse warranty;
    private ServiceRequestResponse serviceRequest;
    private PurchaseResponse purchase;
    private List<StatusHistoryResponse> history;

    public ServiceRequestHistoryResponse(ProductResponse product, WarrantyResponse warranty,
                                         ServiceRequestResponse serviceRequest,
                                         PurchaseResponse purchase,
                                         List<StatusHistoryResponse> history) {
        this.product = product;
        this.warranty = warranty;
        this.serviceRequest = serviceRequest;
        this.purchase = purchase;
        this.history = history;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public WarrantyResponse getWarranty() {
        return warranty;
    }

    public void setWarranty(WarrantyResponse warranty) {
        this.warranty = warranty;
    }

    public ServiceRequestResponse getServiceRequest() {
        return serviceRequest;
    }

    public void setServiceRequest(ServiceRequestResponse serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    public PurchaseResponse getPurchase() {
        return purchase;
    }

    public void setPurchase(PurchaseResponse purchase) {
        this.purchase = purchase;
    }

    public List<StatusHistoryResponse> getHistory() {
        return history;
    }

    public void setHistory(List<StatusHistoryResponse> history) {
        this.history = history;
    }
}
