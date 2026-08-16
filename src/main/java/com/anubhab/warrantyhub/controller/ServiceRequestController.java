package com.anubhab.warrantyhub.controller;

import com.anubhab.warrantyhub.dto.ServiceRequestCreateRequest;
import com.anubhab.warrantyhub.dto.ServiceRequestResponse;
import com.anubhab.warrantyhub.dto.StatusChangeRequest;
import com.anubhab.warrantyhub.dto.StatusHistoryResponse;
import com.anubhab.warrantyhub.service.ServiceRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-requests")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    public ServiceRequestController(
            ServiceRequestService serviceRequestService) {

        this.serviceRequestService = serviceRequestService;
    }

    @PostMapping
    public ResponseEntity<ServiceRequestResponse> createRequest(
            @Valid @RequestBody ServiceRequestCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceRequestService.createRequest(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestResponse> getServiceRequest(@PathVariable Long id) {
        return ResponseEntity.ok(serviceRequestService.getServiceRequest(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ServiceRequestResponse> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusChangeRequest request) {

        return ResponseEntity.ok(serviceRequestService.changeStatus(id, request));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<StatusHistoryResponse>> getStatusHistory(@PathVariable Long id) {
        return ResponseEntity.ok(serviceRequestService.getStatusHistory(id));
    }
}