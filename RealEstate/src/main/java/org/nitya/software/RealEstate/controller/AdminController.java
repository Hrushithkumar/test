package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.model.User;
import org.nitya.software.RealEstate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RolesAllowed("ROLE_ADMIN")
public class AdminController {

    private final UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userDetails.getUsername());
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


}
