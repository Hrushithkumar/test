package org.nitya.software.RealEstate.service;

import org.nitya.software.RealEstate.model.Employee;
import org.nitya.software.RealEstate.model.User;
import org.nitya.software.RealEstate.repository.EmployeeRepository;
import org.nitya.software.RealEstate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(email);
        if(userOptional.isPresent()){
            //MyUserDetails mud = new MyUserDetails(userOptional.get());
            return new MyUserDetails(userOptional.get());
                    //new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), new ArrayList<>());
        } else if (employeeOptional.isPresent()) {
            return new MyUserDetails(employeeOptional.get());
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }
}