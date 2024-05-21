package org.nitya.software.RealEstate.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, unique = true)
    private String username;

    @NonNull
    @Length(min = 6, max = 16)
    @Column(nullable = false)
    private String password;

    @NonNull
    @Column(nullable = false)
    @Transient
    private String confirmPassword;

    @NonNull
    @Column(nullable = false, unique = true)
    private String email;

    @NonNull
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String phoneNumber;
}
