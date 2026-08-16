package com.anubhab.warrantyhub.dto;

public class CompanyResponse {

    private Long companyId;
    private String companyName;
    private String email;
    private String phone;
    private String status;

    public CompanyResponse(Long companyId, String companyName, String email, String phone, String status) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }
}