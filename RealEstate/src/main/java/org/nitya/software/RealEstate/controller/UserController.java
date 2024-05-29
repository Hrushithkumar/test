package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.dto.LoginRequest;
import org.nitya.software.RealEstate.exception.CustomExceptions.*;
import org.nitya.software.RealEstate.model.User;
import org.nitya.software.RealEstate.model.UserRole;
import org.nitya.software.RealEstate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            if(userService.findByUsername(user.getUsername()).isPresent()){
                throw new UserAlreadyExistsException("User already exists");
            }
            if(userService.findByEmail(user.getEmail()).isPresent()){
                throw new EmailAlreadyExistsException("Email already exists");
            }
            if(userService.findByPhoneNumber(user.getPhoneNumber()).isPresent()){
                throw new PhoneNumberAlreadyExistsException("Phone number already exists");
            }
            if(userService.findByLastName(user.getLastName()).isPresent()){
                throw new LastNameAlreadyExistsException("Last name already exists");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(UserRole.ROLE_USER);
            User savedUser = userService.save(user);
            response.put("message", "User created successfully");
            response.put("user", savedUser);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

        }
        catch (UserAlreadyExistsException | EmailAlreadyExistsException |
                 PhoneNumberAlreadyExistsException | LastNameAlreadyExistsException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        catch (Exception e) {
            response.put("error" ,"An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@Valid @PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setEmail(userDetails.getEmail());
            user.setPhoneNumber(userDetails.getPhoneNumber());
            User updatedUser = userService.save(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            userService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> user = userService.findByEmail(loginRequest.getEmail());
        Map<String, Object> response = new HashMap<>();
        if (user.isPresent()){
            if (passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
                response.put("message" ,"Login successful");
                return ResponseEntity.status(200).body(response);
            } else {
                response.put("error", "Invalid email or password");
                return ResponseEntity.status(401).body(response);
            }
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

}
