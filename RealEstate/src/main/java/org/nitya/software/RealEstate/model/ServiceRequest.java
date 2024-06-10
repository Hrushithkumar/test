package org.nitya.software.RealEstate.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ServiceRequest {

    //Todo: Add validations
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String serviceRequested;
    private String description;
    private String location;
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'unresolved'")
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
