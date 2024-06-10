package org.nitya.software.RealEstate.service;

import org.nitya.software.RealEstate.model.ServiceRequest;
import org.nitya.software.RealEstate.model.User;
import org.nitya.software.RealEstate.repository.ServiceRequestRepository;
import org.nitya.software.RealEstate.repository.UserRepository;
import org.nitya.software.RealEstate.utils.LoggedInUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRequestService {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoggedInUserUtil loggedInUserUtil;

    public List<ServiceRequest> getAllServiceRequests(){
        return serviceRequestRepository.findAll();
    }

    public ServiceRequest createServiceRequest(ServiceRequest serviceRequest) {
        String username = loggedInUserUtil.getCurrentUserEmail();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found" + username));
        if(serviceRequest.getStatus() == null){
            serviceRequest.setStatus("unresolved");
        }
        serviceRequest.setUser(user);
        return serviceRequestRepository.save(serviceRequest);
    }

    public  List<ServiceRequest> getServiceRequestsByUserId(){
        String username = loggedInUserUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with " + username));
        return serviceRequestRepository.findByUserId(user.getId());
    }

}
