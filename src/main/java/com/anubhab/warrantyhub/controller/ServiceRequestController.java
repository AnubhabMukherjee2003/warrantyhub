package com.anubhab.warrantyhub.controller;
import com.anubhab.warrantyhub.model.ServiceRequest;
import com.anubhab.warrantyhub.dto.ServiceRequestCreateRequest;
import com.anubhab.warrantyhub.service.ServiceRequestService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service-requests")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    public ServiceRequestController(
            ServiceRequestService serviceRequestService) {

        this.serviceRequestService = serviceRequestService;
    }

    @PostMapping
    public ServiceRequest createRequest(
            @RequestBody ServiceRequestCreateRequest request) {

        return serviceRequestService.createRequest(request);
    }
}