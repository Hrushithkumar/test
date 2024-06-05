package org.nitya.software.RealEstate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.nitya.software.RealEstate.exception.UserValidationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Collections;
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
        @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
        private Set<Role> roles;

//    public void validate() {
//        if (username == null || firstName == null || lastName == null ||
//                password == null || email == null || phoneNumber == null) {
//            throw new UserValidationException("All fields are mandatory and cannot be null.");
//        }
//    }

}
