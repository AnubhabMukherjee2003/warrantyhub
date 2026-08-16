package com.anubhab.warrantyhub.dto;

public class CustomerResponse {

    private Long customerId;
    private String name;
    private String email;
    private String phone;

    public CustomerResponse(
            Long customerId,
            String name,
            String email,
            String phone) {

        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}