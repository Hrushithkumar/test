package org.nitya.software.RealEstate.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ContactForm {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String message;

}
