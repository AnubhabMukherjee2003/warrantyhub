package com.anubhab.warrantyhub.dto;

import java.time.LocalDate;

public class PurchaseResponse {

    private Long purchaseId;
    private Long customerId;
    private Long productId;
    private Long warrantyId;
    private Long companyId;
    private LocalDate purchaseDate;
    private String invoiceNumber;

    public PurchaseResponse(Long purchaseId, Long customerId, Long productId, Long warrantyId,
                            Long companyId, LocalDate purchaseDate, String invoiceNumber) {
        this.purchaseId = purchaseId;
        this.customerId = customerId;
        this.productId = productId;
        this.warrantyId = warrantyId;
        this.companyId = companyId;
        this.purchaseDate = purchaseDate;
        this.invoiceNumber = invoiceNumber;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getWarrantyId() {
        return warrantyId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }
}