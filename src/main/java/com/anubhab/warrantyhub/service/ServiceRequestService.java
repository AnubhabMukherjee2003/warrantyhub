package com.anubhab.warrantyhub.service;

import com.anubhab.warrantyhub.model.Purchase;
import com.anubhab.warrantyhub.model.RequestStatusHistory;
import com.anubhab.warrantyhub.model.ServiceRequest;
import com.anubhab.warrantyhub.dto.ServiceRequestCreateRequest;
import com.anubhab.warrantyhub.repository.PurchaseRepository;
import com.anubhab.warrantyhub.repository.RequestStatusHistoryRepository;
import com.anubhab.warrantyhub.repository.ServiceRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final PurchaseRepository purchaseRepository;
    private final RequestStatusHistoryRepository historyRepository;

    public ServiceRequestService(
            ServiceRequestRepository serviceRequestRepository,
            PurchaseRepository purchaseRepository,
            RequestStatusHistoryRepository historyRepository) {

        this.serviceRequestRepository = serviceRequestRepository;
        this.purchaseRepository = purchaseRepository;
        this.historyRepository = historyRepository;
    }

    @Transactional
    public ServiceRequest createRequest(
            ServiceRequestCreateRequest request) {

        Purchase purchase = purchaseRepository
                .findById(request.getPurchaseId())
                .orElseThrow(() ->
                        new RuntimeException("Purchase not found"));

        ServiceRequest serviceRequest = new ServiceRequest();

        serviceRequest.setPurchase(purchase);
        serviceRequest.setIssueCategory(request.getIssueCategory());
        serviceRequest.setIssueDescription(request.getIssueDescription());
        serviceRequest.setPriority(request.getPriority());
        serviceRequest.setCurrentStatus("OPEN");

        LocalDateTime now = LocalDateTime.now();

        serviceRequest.setCreatedAt(now);
        serviceRequest.setUpdatedAt(now);

        ServiceRequest savedRequest =
                serviceRequestRepository.save(serviceRequest);

        RequestStatusHistory history =
                new RequestStatusHistory();

        history.setRequest(savedRequest);
        history.setStatus("OPEN");
        history.setRemarks("Service request created");
        history.setChangedBy("CUSTOMER");
        history.setChangedAt(now);

        historyRepository.save(history);

        return savedRequest;
    }
}
