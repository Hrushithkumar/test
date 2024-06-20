package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.dto.ServiceRequestDto;
import org.nitya.software.RealEstate.model.ServiceRequest;
import org.nitya.software.RealEstate.service.ServiceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/servicerequest")
public class ServiceRequestController {

    @Autowired
    private ServiceRequestService serviceRequestService;

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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteServiceRequestById(@PathVariable Long id){
        Boolean isServiceRequestAvailable = serviceRequestService.isServiceReqPresent(id);
        if(isServiceRequestAvailable){
            serviceRequestService.deleteServiceRequestsById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<ServiceRequestDto> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate){
        String status = statusUpdate.get("status");
        Optional<ServiceRequest> updateServiceRequest = serviceRequestService.updateStatus(id, status);
        if(updateServiceRequest.isPresent()){

            ServiceRequestDto dto = new ServiceRequestDto();
            ServiceRequest updatedServiceRequest = updateServiceRequest.get();
            dto.setId(updatedServiceRequest.getId());
            dto.setName(updatedServiceRequest.getName());
            dto.setEmail(updatedServiceRequest.getEmail());
            dto.setPhone(updatedServiceRequest.getPhone());
            dto.setServiceRequested(updatedServiceRequest.getServiceRequested());
            dto.setDescription(updatedServiceRequest.getDescription());
            dto.setLocation(updatedServiceRequest.getLocation());
            dto.setStatus(updatedServiceRequest.getStatus());
            dto.setUserID(updatedServiceRequest.getUser().getId());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }
}
