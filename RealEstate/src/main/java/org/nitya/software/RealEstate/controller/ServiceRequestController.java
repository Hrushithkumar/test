package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.dto.ServiceRequestDto;
import org.nitya.software.RealEstate.model.ServiceRequest;
import org.nitya.software.RealEstate.service.ServiceRequestService;
import org.nitya.software.RealEstate.utils.LoggedInUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/servicerequest")
public class ServiceRequestController {

    @Autowired
    private ServiceRequestService serviceRequestService;

    @Autowired
    private LoggedInUserUtil loggedInUserUtil;

    @GetMapping
    public ResponseEntity<List<ServiceRequestDto>> getAllServiceRequests(){
        List<ServiceRequest> serviceRequests = serviceRequestService.getAllServiceRequests();
        List<ServiceRequestDto> serviceRequestDtoList = new ArrayList<>();

        updateServiceRequestDto(serviceRequests, serviceRequestDtoList);
        return new ResponseEntity<>(serviceRequestDtoList, HttpStatus.OK);
    }

    private static void updateServiceRequestDto(List<ServiceRequest> serviceRequests, List<ServiceRequestDto> serviceRequestDtoList) {
        for(ServiceRequest serviceRequest: serviceRequests){
            ServiceRequestDto serviceRequestDto = new ServiceRequestDto();
            serviceRequestDto.setEmail(serviceRequest.getEmail());
            serviceRequestDto.setName(serviceRequest.getName());
            serviceRequestDto.setPhone(serviceRequest.getPhone());
            serviceRequestDto.setLocation(serviceRequest.getLocation());
            serviceRequestDto.setUserID(serviceRequest.getUser().getId());
            serviceRequestDto.setServiceRequested(serviceRequest.getServiceRequested());
            serviceRequestDto.setDescription(serviceRequest.getDescription());
            serviceRequestDto.setId(serviceRequest.getId());
            serviceRequestDto.setStatus(serviceRequest.getStatus());
            serviceRequestDtoList.add(serviceRequestDto);
        }
    }

    @PostMapping
    public ResponseEntity<ServiceRequest> createServiceRequest(@Valid @RequestBody ServiceRequest serviceRequest) {
            ServiceRequest createdServiceRequest = serviceRequestService.createServiceRequest(serviceRequest);
            return new ResponseEntity<>(createdServiceRequest, HttpStatus.CREATED);
    }

    @GetMapping("/id")
    public ResponseEntity<List<ServiceRequestDto>> getServiceRequestsByUserID(){
        List<ServiceRequest> serviceRequests = serviceRequestService.getServiceRequestsByUserId();
        List<ServiceRequestDto> serviceRequestDtoList = new ArrayList<>();
        updateServiceRequestDto(serviceRequests, serviceRequestDtoList);
        return new ResponseEntity<>(serviceRequestDtoList, HttpStatus.OK);
    }
}
