package org.nitya.software.RealEstate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}
