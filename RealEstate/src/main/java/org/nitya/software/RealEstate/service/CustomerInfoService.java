package org.nitya.software.RealEstate.service;

import org.nitya.software.RealEstate.model.CustomerInfo;
import org.nitya.software.RealEstate.repository.CustomerInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerInfoService {

    @Autowired
    private final CustomerInfoRepository customerInfoRepository;

    @Autowired
    public CustomerInfoService(CustomerInfoRepository customerInfoRepository) {
        this.customerInfoRepository = customerInfoRepository;
    }

    public CustomerInfo save(CustomerInfo customerInfo) {
        return customerInfoRepository.save(customerInfo);
    }

    public List<CustomerInfo> findAll() {
        return customerInfoRepository.findAll();
    }

    public Optional<CustomerInfo> findById(Long id) {
        return customerInfoRepository.findById(id);
    }

    public void deleteById(Long id) {
        customerInfoRepository.deleteById(id);
    }

    public List<CustomerInfo> findDealsByDateRange(LocalDate startDate, LocalDate endDate) {
        return customerInfoRepository.findByDealDateBetween(startDate, endDate);
    }


}
