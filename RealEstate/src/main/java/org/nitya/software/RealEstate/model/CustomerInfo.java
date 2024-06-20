package org.nitya.software.RealEstate.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class CustomerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String customerAddress;

    @Column(nullable = false)
    private LocalDate dealDate;

    @Column(nullable = false)
    private float dealValue;
}
