package org.nitya.software.RealEstate.service;

import org.nitya.software.RealEstate.model.ServiceRequest;
import org.nitya.software.RealEstate.repository.ServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRequestService {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    public List<ServiceRequest> getAllServiceRequests(){
        return serviceRequestRepository.findAll();
    }

    public ServiceRequest createServiceRequest(ServiceRequest serviceRequest){
        return serviceRequestRepository.save(serviceRequest);
    }
}
