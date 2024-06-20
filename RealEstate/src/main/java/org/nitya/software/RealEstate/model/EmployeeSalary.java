package org.nitya.software.RealEstate.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
public class EmployeeSalary {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String employeeName;

    @Column(nullable = false)
    private int payRate;

    @Column(nullable = false)
    private int hours;

    @Column(nullable = false)
    private int salary;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

}
