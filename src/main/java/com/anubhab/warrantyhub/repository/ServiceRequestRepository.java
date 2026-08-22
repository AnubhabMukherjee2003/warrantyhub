package com.anubhab.warrantyhub.repository;

import com.anubhab.warrantyhub.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByPurchase_Product_Company_Email(String email);
    List<ServiceRequest> findByPurchase_Customer_Email(String email);
}