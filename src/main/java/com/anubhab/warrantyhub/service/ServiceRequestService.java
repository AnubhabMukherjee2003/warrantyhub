package com.anubhab.warrantyhub.service;

import com.anubhab.warrantyhub.dto.StatusChangeRequest;
import com.anubhab.warrantyhub.dto.StatusHistoryResponse;
import com.anubhab.warrantyhub.dto.ServiceRequestResponse;
import com.anubhab.warrantyhub.exception.PurchaseNotFoundException;
import com.anubhab.warrantyhub.exception.ServiceRequestNotFoundException;
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
import java.util.List;

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
    public ServiceRequestResponse createRequest(ServiceRequestCreateRequest request) {

        Purchase purchase = purchaseRepository
                .findById(request.getPurchaseId())
                .orElseThrow(() -> new PurchaseNotFoundException(request.getPurchaseId()));

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

        return toResponse(savedRequest);
    }

    @Transactional(readOnly = true)
    public ServiceRequestResponse getServiceRequest(Long id) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new ServiceRequestNotFoundException(id));
        return toResponse(serviceRequest);
    }

    @Transactional
    public ServiceRequestResponse changeStatus(Long id, StatusChangeRequest request) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new ServiceRequestNotFoundException(id));

        serviceRequest.setCurrentStatus(request.getStatus());
        serviceRequest.setUpdatedAt(LocalDateTime.now());

        ServiceRequest savedRequest = serviceRequestRepository.save(serviceRequest);

        RequestStatusHistory history = new RequestStatusHistory();
        history.setRequest(savedRequest);
        history.setStatus(request.getStatus());
        history.setRemarks(request.getRemarks());
        history.setChangedBy(request.getChangedBy());
        history.setChangedAt(savedRequest.getUpdatedAt());
        historyRepository.save(history);

        return toResponse(savedRequest);
    }

    @Transactional(readOnly = true)
    public List<StatusHistoryResponse> getStatusHistory(Long id) {
        if (!serviceRequestRepository.existsById(id)) {
            throw new ServiceRequestNotFoundException(id);
        }

        return historyRepository.findByRequest_RequestIdOrderByChangedAtAsc(id)
                .stream()
                .map(history -> new StatusHistoryResponse(
                        history.getHistoryId(),
                        history.getRequest().getRequestId(),
                        history.getStatus(),
                        history.getRemarks(),
                        history.getChangedBy(),
                        history.getChangedAt()
                ))
                .toList();
    }

    private ServiceRequestResponse toResponse(ServiceRequest serviceRequest) {
        return new ServiceRequestResponse(
                serviceRequest.getRequestId(),
                serviceRequest.getPurchase().getPurchaseId(),
                serviceRequest.getIssueCategory(),
                serviceRequest.getIssueDescription(),
                serviceRequest.getPriority(),
                serviceRequest.getCurrentStatus(),
                serviceRequest.getCreatedAt(),
                serviceRequest.getUpdatedAt()
        );
    }
}
