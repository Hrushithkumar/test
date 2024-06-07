package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.model.ServiceRequest;
import org.nitya.software.RealEstate.service.ServiceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/servicerequest")
public class ServiceRequestController {

    @Autowired
    private ServiceRequestService serviceRequestService;

    @GetMapping
    public ResponseEntity<List<ServiceRequest>> getAllServiceRequests(){
        List<ServiceRequest> serviceRequests = serviceRequestService.getAllServiceRequests();
        return new ResponseEntity<>(serviceRequests, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ServiceRequest> createServiceRequest(@Valid @RequestBody ServiceRequest serviceRequest){
        ServiceRequest createdServiceRequest = serviceRequestService.createServiceRequest(serviceRequest);
        return new ResponseEntity<>(createdServiceRequest, HttpStatus.CREATED);
    }
}
