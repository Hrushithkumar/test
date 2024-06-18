package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.exception.CustomExceptions;
import org.nitya.software.RealEstate.model.Employee;
import org.nitya.software.RealEstate.model.User;
import org.nitya.software.RealEstate.repository.RoleRepository;
import org.nitya.software.RealEstate.service.EmployeeService;
import org.nitya.software.RealEstate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
//@RolesAllowed("ROLE_ADMIN")
public class AdminController {

    private final UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

   // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userDetails.getUsername());
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            user.setEmail(userDetails.getEmail());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            user.setUpdatedAt(LocalDate.now());
            User updatedUser = userService.save(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/employee/register")
    public ResponseEntity<?> register(@Valid @RequestBody Employee employee) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (employeeService.findByUsername(employee.getUsername()).isPresent()) {
                throw new CustomExceptions.UserAlreadyExistsException("User already exists");
            }
            if (employeeService.findByEmail(employee.getEmail()).isPresent()) {
                throw new CustomExceptions.EmailAlreadyExistsException("Email already exists");
            }
            if (employeeService.findByPhoneNumber(employee.getPhoneNumber()).isPresent()) {
                throw new CustomExceptions.PhoneNumberAlreadyExistsException("Phone number already exists");
            }
            if (employeeService.findByLastName(employee.getLastName()).isPresent()) {
                throw new CustomExceptions.LastNameAlreadyExistsException("Last name already exists");
            }

            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            employee.setRoles(Collections.singleton(roleRepository.findByName("ROLE_EMPLOYEE")));
            LocalDate now = LocalDate.now();
            employee.setCreatedAt(now);
            employee.setUpdatedAt(now);
            Employee savedEmployee = employeeService.save(employee);
            response.put("message", "Employee created successfully");
            response.put("employee", savedEmployee);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (CustomExceptions.UserAlreadyExistsException | CustomExceptions.EmailAlreadyExistsException |
               CustomExceptions.PhoneNumberAlreadyExistsException | CustomExceptions.LastNameAlreadyExistsException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        catch (Exception e) {
            response.put("error" ,"An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@Valid @PathVariable Long id, @RequestBody Employee employeeDetails) {
        Optional<Employee> userOptional = employeeService.findById(id);
        if (userOptional.isPresent()) {
            Employee employee = userOptional.get();
            employee.setUsername(employeeDetails.getUsername());
            employee.setFirstName(employeeDetails.getFirstName());
            employee.setLastName(employeeDetails.getLastName());
            employee.setPassword(passwordEncoder.encode(employeeDetails.getPassword()));
            employee.setJobTitle(employeeDetails.getJobTitle());
            employee.setEmail(employeeDetails.getEmail());
            employee.setPhoneNumber(employeeDetails.getPhoneNumber());
            employee.setUpdatedAt(LocalDate.now());
            Employee updatedEmployee = employeeService.save(employee);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        Optional<Employee> userOptional = employeeService.findById(id);
        if (userOptional.isPresent()) {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
