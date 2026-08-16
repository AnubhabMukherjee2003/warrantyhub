package com.anubhab.warrantyhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "warranty")
public class Warranty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long warrantyId;

    private Integer warrantyPeriod;
    private String warrantyUnit;
    private String terms;
    public Long getWarrantyId() {
        return warrantyId;
    }
    public void setWarrantyId(Long warrantyId) {
        this.warrantyId = warrantyId;
    }
    public Integer getWarrantyPeriod() {
        return warrantyPeriod;
    }
    public void setWarrantyPeriod(Integer warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }
    public String getWarrantyUnit() {
        return warrantyUnit;
    }
    public void setWarrantyUnit(String warrantyUnit) {
        this.warrantyUnit = warrantyUnit;
    }
    public String getTerms() {
        return terms;
    }
    public void setTerms(String terms) {
        this.terms = terms;
    }

    // getters and setters
}