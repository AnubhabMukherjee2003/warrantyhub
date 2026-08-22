package com.anubhab.warrantyhub.service;

import com.anubhab.warrantyhub.dto.StatusChangeRequest;
import com.anubhab.warrantyhub.dto.StatusHistoryResponse;
import com.anubhab.warrantyhub.dto.ServiceRequestResponse;
import com.anubhab.warrantyhub.dto.ServiceRequestHistoryResponse;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;

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

                String authenticatedEmail = getAuthenticatedUserEmail();
                String ownerEmail = purchase.getCustomer().getEmail();
                if (!authenticatedEmail.equals(ownerEmail)) {
                        throw new AccessDeniedException(
                                        "You do not have access to this purchase");
                }

                ServiceRequest serviceRequest = new ServiceRequest();

                serviceRequest.setPurchase(purchase);
                serviceRequest.setIssueCategory(request.getIssueCategory());
                serviceRequest.setIssueDescription(request.getIssueDescription());
                serviceRequest.setPhotoUrl(request.getPhotoUrl());
                serviceRequest.setVideoUrl(request.getVideoUrl());

                LocalDateTime now = LocalDateTime.now();

                serviceRequest.setCreatedAt(now);
                serviceRequest.setUpdatedAt(now);

                ServiceRequest savedRequest = serviceRequestRepository.save(serviceRequest);

                RequestStatusHistory history = new RequestStatusHistory();

                history.setRequest(savedRequest);
                history.setStatus("OPEN");
                history.setRemarks("Service request created");
                history.setChangedBy(authenticatedEmail);
                history.setChangedAt(now);

                historyRepository.save(history);

                return toResponse(savedRequest);
        }

        @Transactional(readOnly = true)
        public ServiceRequestResponse getServiceRequest(Long id) {
                ServiceRequest request = getRequestForCustomer(id);
                return toResponse(request);
        }

        @Transactional(readOnly = true)
        public List<ServiceRequestResponse> getCompanyServiceRequests(String email) {
                return serviceRequestRepository.findByPurchase_Product_Company_Email(email)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        @Transactional(readOnly = true)
        public List<ServiceRequestResponse> getCustomerServiceRequests(String email) {
                return serviceRequestRepository.findByPurchase_Customer_Email(email)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        @Transactional
        public ServiceRequestResponse changeStatus(Long id, StatusChangeRequest request) {
                ServiceRequest serviceRequest = serviceRequestRepository.findById(id)
                                .orElseThrow(() -> new ServiceRequestNotFoundException(id));

                String authenticatedEmail = getAuthenticatedUserEmail();
                String companyEmail = serviceRequest.getPurchase().getProduct().getCompany().getEmail();
                if (!authenticatedEmail.equals(companyEmail)) {
                        throw new AccessDeniedException(
                                        "You do not have access to this service request");
                }

                serviceRequest.setUpdatedAt(LocalDateTime.now());

                ServiceRequest savedRequest = serviceRequestRepository.save(serviceRequest);

                RequestStatusHistory history = new RequestStatusHistory();
                history.setRequest(savedRequest);
                history.setStatus(request.getStatus());
                history.setRemarks(request.getRemarks());
                history.setChangedBy(authenticatedEmail);
                history.setChangedAt(savedRequest.getUpdatedAt());
                historyRepository.save(history);

                return toResponse(savedRequest);
        }

        @Transactional(readOnly = true)
        public ServiceRequestHistoryResponse getStatusHistory(Long id) {
                ServiceRequest request = getRequestForOwnerOrCompany(id);

                List<StatusHistoryResponse> historyList = historyRepository.findByRequest_RequestIdOrderByChangedAtAsc(id)
                                .stream()
                                .map(history -> new StatusHistoryResponse(
                                                history.getHistoryId(),
                                                history.getStatus(),
                                                history.getRemarks(),
                                                history.getChangedBy(),
                                                history.getChangedAt()))
                                .toList();

                return new ServiceRequestHistoryResponse(toResponse(request), historyList);
        }

        private ServiceRequestResponse toResponse(ServiceRequest serviceRequest) {
                String currentStatus = historyRepository.findFirstByRequest_RequestIdOrderByChangedAtDesc(serviceRequest.getRequestId())
                                .map(RequestStatusHistory::getStatus)
                                .orElse("UNKNOWN");

                return new ServiceRequestResponse(
                                serviceRequest.getRequestId(),
                                serviceRequest.getPurchase().getPurchaseId(),
                                serviceRequest.getIssueCategory(),
                                serviceRequest.getIssueDescription(),
                                serviceRequest.getPhotoUrl(),
                                serviceRequest.getVideoUrl(),
                                currentStatus,
                                serviceRequest.getCreatedAt(),
                                serviceRequest.getUpdatedAt());
        }

        private String getAuthenticatedUserEmail() {

                Authentication authentication = SecurityContextHolder
                                .getContext()
                                .getAuthentication();

                return authentication.getName();
        }

        private ServiceRequest getRequestForCustomer(Long id) {

                ServiceRequest request = serviceRequestRepository.findById(id)
                                .orElseThrow(() -> new ServiceRequestNotFoundException(id));

                String authenticatedEmail = getAuthenticatedUserEmail();

                String ownerEmail = request.getPurchase()
                                .getCustomer()
                                .getEmail();

                if (!authenticatedEmail.equals(ownerEmail)) {
                        throw new AccessDeniedException(
                                        "You do not have access to this service request");
                }

                return request;
        }

        private ServiceRequest getRequestForOwnerOrCompany(Long id) {
                ServiceRequest request = serviceRequestRepository.findById(id)
                                .orElseThrow(() -> new ServiceRequestNotFoundException(id));

                String authenticatedEmail = getAuthenticatedUserEmail();
                String customerEmail = request.getPurchase().getCustomer().getEmail();
                String companyEmail = request.getPurchase().getProduct().getCompany().getEmail();

                if (!authenticatedEmail.equals(customerEmail) && !authenticatedEmail.equals(companyEmail)) {
                        throw new AccessDeniedException("You do not have access to this service request");
                }

                return request;
        }
}
