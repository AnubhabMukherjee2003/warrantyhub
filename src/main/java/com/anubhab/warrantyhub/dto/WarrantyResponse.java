package com.anubhab.warrantyhub.dto;

public class WarrantyResponse {
    private Long warrantyId;

    private Integer warrantyPeriod;
    private String warrantyUnit;
    private String terms;

    public WarrantyResponse(Long warrantyId, Integer warrantyPeriod, String warrantyUnit, String terms) {
        this.warrantyId = warrantyId;
        this.warrantyPeriod = warrantyPeriod;
        this.warrantyUnit = warrantyUnit;
        this.terms = terms;
    }
    public Long getWarrantyId() {
        return warrantyId;
    }

    public Integer getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public String getWarrantyUnit() {
        return warrantyUnit;
    }

    public String getTerms() {
        return terms;
    }
}
