package org.nitya.software.RealEstate.model;

import lombok.Getter;
import lombok.Setter;
import org.nitya.software.RealEstate.model.enums.Category;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = true)
    private String image;

    @Column(nullable = false)
    private float price;

    @Column(nullable = false)
    private LocalDate createdOn;
}
