package org.nitya.software.RealEstate.service;

import org.nitya.software.RealEstate.model.EmployeeSalary;
import org.nitya.software.RealEstate.repository.EmployeeSalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeSalaryService {

    private final EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    public EmployeeSalaryService(EmployeeSalaryRepository employeeSalaryRepository) {
        this.employeeSalaryRepository = employeeSalaryRepository;
    }

    public List<EmployeeSalary> findAll() {
        return employeeSalaryRepository.findAll();
    }

    public Optional<EmployeeSalary> findById(Long id) {
        return employeeSalaryRepository.findById(id);
    }

    public EmployeeSalary save(EmployeeSalary employeeSalary) {
        return employeeSalaryRepository.save(employeeSalary);
    }

    public void deleteById(Long id) {
        employeeSalaryRepository.deleteById(id);
    }
}
