package com.anubhab.warrantyhub.dto;

public class ProductResponse {

    private Long purchaseId;
    private Long productId;
    private String productName;
    private String category;
    private String modelNumber;
    private Long companyId;
    private String companyName;

    public ProductResponse(Long purchaseId, Long productId, String productName, String category, String modelNumber,
                           Long companyId, String companyName) {
        this.purchaseId = purchaseId;
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.modelNumber = modelNumber;
        this.companyId = companyId;
        this.companyName = companyName;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
