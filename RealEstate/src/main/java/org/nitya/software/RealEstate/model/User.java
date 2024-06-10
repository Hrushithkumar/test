package org.nitya.software.RealEstate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Setter
@Entity
    @Table(name = "users")
    public class User{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NonNull
        @Column(nullable = false, unique = true)
        private String username;

        @NonNull
        @Column(nullable = false)
        private String firstName;

        @NonNull
        @Column(nullable = false, unique = true)
        private String lastName;

        @NonNull
        //@Length(min = 6, max = 16)
        @Column(nullable = false)
        private String password;

        @NonNull
        @Column(nullable = false, unique = true)
        private String email;

        @NonNull
        @Column(nullable = false, unique = true)
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
        private String phoneNumber;

        @Column(nullable = false)
        @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
        private Set<Role> roles;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnoreProperties("user")
        private Set<ServiceRequest> serviceRequests;

}
