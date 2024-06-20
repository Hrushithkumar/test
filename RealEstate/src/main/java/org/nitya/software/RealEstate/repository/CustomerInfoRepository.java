package org.nitya.software.RealEstate.repository;

import org.nitya.software.RealEstate.model.CustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Long> {

    List<CustomerInfo> findByDealDateBetween(LocalDate startDate, LocalDate endDate);

}
