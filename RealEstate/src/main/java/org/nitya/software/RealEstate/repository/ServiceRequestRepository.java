package org.nitya.software.RealEstate.repository;

import lombok.NonNull;
import org.nitya.software.RealEstate.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByUserId(Long userId);

    @Override
    boolean existsById(@NonNull Long serviceRequest);


}
