package com.anubhab.warrantyhub.repository;

import com.anubhab.warrantyhub.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRequestRepository
        extends JpaRepository<ServiceRequest, Long> {
}