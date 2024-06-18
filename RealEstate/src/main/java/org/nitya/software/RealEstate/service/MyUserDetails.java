package org.nitya.software.RealEstate.service;

import lombok.Getter;
import org.nitya.software.RealEstate.model.Employee;
import org.nitya.software.RealEstate.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Getter
public class MyUserDetails implements UserDetails {

    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

//    public MyUserDetails(User user) {
//        this.user = user;
//    }

    public MyUserDetails(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public MyUserDetails(Employee employee) {
        this.email = employee.getEmail();
        this.password = employee.getPassword();
        this.authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<? extends GrantedAuthority> authorities =  user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//
//        System.out.println("Roles assigned to user: " + user.getUsername());
//        for (GrantedAuthority authority : authorities) {
//            System.out.println(authority.getAuthority());
//        }
//
//        return authorities;
//    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}