package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.model.CustomerInfo;
import org.nitya.software.RealEstate.service.CustomerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Create a new customer info record
    @PostMapping("/create")
    public ResponseEntity<CustomerInfo> createCustomerInfo(@RequestBody CustomerInfo customerInfo) {
        try {
            CustomerInfo newCustomerInfo = customerInfoService.save(customerInfo);
            return new ResponseEntity<>(newCustomerInfo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Retrieve all customer info records
    @GetMapping("/list")
    public ResponseEntity<List<CustomerInfo>> getAllCustomerInfos() {
        List<CustomerInfo> customerInfos = customerInfoService.findAll();
        if (customerInfos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(customerInfos, HttpStatus.OK);
    }

    // Retrieve a single customer info record by ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerInfo> getCustomerInfoById(@PathVariable Long id) {
        Optional<CustomerInfo> customerInfo = customerInfoService.findById(id);
        return customerInfo.map(info -> new ResponseEntity<>(info, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a customer info record by ID
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

    // Delete a customer info record by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteCustomerInfo(@PathVariable Long id) {
        try {
            customerInfoService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/track-deals")
    public ResponseEntity<Map<String, Object>> trackDeals(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<CustomerInfo> deals = customerInfoService.findDealsByDateRange(startDate, endDate);

        int totalDeals = deals.size();
        float totalValue = (float) deals.stream().mapToDouble(CustomerInfo::getDealValue).sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalDeals", totalDeals);
        summary.put("totalValue", totalValue);

        return ResponseEntity.ok(summary);
    }



}
