package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.model.EmployeeSalary;
import org.nitya.software.RealEstate.service.EmployeeSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employeesalary/")
public class EmployeeSalaryController {

    private final EmployeeSalaryService employeeSalaryService;

    @Autowired
    public EmployeeSalaryController(EmployeeSalaryService employeeSalaryService) {
        this.employeeSalaryService = employeeSalaryService;
    }

    @GetMapping
    public List<EmployeeSalary> getAllEmployeeSalaries() {
        return employeeSalaryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeSalary> getEmployeeSalaryById(@PathVariable Long id) {
        Optional<EmployeeSalary> employeeSalary = employeeSalaryService.findById(id);
        return employeeSalary.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public EmployeeSalary createEmployeeSalary(@RequestBody EmployeeSalary employeeSalary) {
        return employeeSalaryService.save(employeeSalary);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeSalary> updateEmployeeSalary(@PathVariable Long id, @RequestBody EmployeeSalary employeeSalary) {
        if (employeeSalaryService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employeeSalary.setId(id);
        return ResponseEntity.ok(employeeSalaryService.save(employeeSalary));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeSalary(@PathVariable Long id) {
        if (employeeSalaryService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employeeSalaryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
