package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.dto.DateRangeDto;
import org.nitya.software.RealEstate.model.CustomerInfo;
import org.nitya.software.RealEstate.service.CustomerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/customer-info")
public class CustomerInfoController {

    @Autowired
    private CustomerInfoService customerInfoService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @PostMapping("/create")
    public ResponseEntity<CustomerInfo> createCustomerInfo(@RequestBody CustomerInfo customerInfo) {
        try {
            CustomerInfo newCustomerInfo = customerInfoService.save(customerInfo);
            return new ResponseEntity<>(newCustomerInfo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @GetMapping("/list")
    public ResponseEntity<List<CustomerInfo>> getAllCustomerInfos() {
        List<CustomerInfo> customerInfos = customerInfoService.findAll();
        if (customerInfos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(customerInfos, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerInfo> getCustomerInfoById(@PathVariable Long id) {
        Optional<CustomerInfo> customerInfo = customerInfoService.findById(id);
        return customerInfo.map(info -> new ResponseEntity<>(info, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerInfo> updateCustomerInfo(@PathVariable Long id, @RequestBody CustomerInfo customerInfo) {
        Optional<CustomerInfo> customerInfoData = customerInfoService.findById(id);
        if (customerInfoData.isPresent()) {
            CustomerInfo _customerInfo = customerInfoData.get();
            _customerInfo.setCustomerName(customerInfo.getCustomerName());
            _customerInfo.setCustomerEmail(customerInfo.getCustomerEmail());
            _customerInfo.setPhoneNumber(customerInfo.getPhoneNumber());
            _customerInfo.setCustomerAddress(customerInfo.getCustomerAddress());
            _customerInfo.setDealDate(customerInfo.getDealDate());
            _customerInfo.setDealValue(customerInfo.getDealValue());
            return new ResponseEntity<>(customerInfoService.save(_customerInfo), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteCustomerInfo(@PathVariable Long id) {
        try {
            customerInfoService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @PostMapping("/track-deals")
    public ResponseEntity<Map<String, Object>> trackDeals(@RequestBody DateRangeDto dateRange) {
        LocalDate startDate = dateRange.getStartDate();
        LocalDate endDate = dateRange.getEndDate();

        List<CustomerInfo> deals = customerInfoService.findDealsByDateRange(startDate, endDate);

        int totalDeals = deals.size();
        float totalValue = (float) deals.stream().mapToDouble(CustomerInfo::getDealValue).sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalDeals", totalDeals);
        summary.put("totalValue", totalValue);

        return ResponseEntity.ok(summary);
    }



}
