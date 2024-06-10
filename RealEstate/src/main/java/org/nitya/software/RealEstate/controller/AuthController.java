package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.dto.LoginRequest;
import org.nitya.software.RealEstate.exception.CustomExceptions.*;
import org.nitya.software.RealEstate.model.User;
import org.nitya.software.RealEstate.repository.RoleRepository;
import org.nitya.software.RealEstate.security.JwtUtil;
import org.nitya.software.RealEstate.service.UserService;
import org.nitya.software.RealEstate.utils.LoggedInUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoggedInUserUtil loggedInUserUtil;

    @GetMapping("/")
    public StreamingResponseBody home(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        InputStream inputStream = getClass().getResourceAsStream("/static/sample.html");
        return outputStream -> {
            byte[] buffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        };
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                throw new UserAlreadyExistsException("User already exists");
            }
            if (userService.findByEmail(user.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException("Email already exists");
            }
            if (userService.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
                throw new PhoneNumberAlreadyExistsException("Phone number already exists");
            }
            if (userService.findByLastName(user.getLastName()).isPresent()) {
                throw new LastNameAlreadyExistsException("Last name already exists");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Collections.singleton(roleRepository.findByName("ROLE_USER")));
            User savedUser = userService.save(user);
            response.put("message", "User created successfully");
            response.put("user", savedUser);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
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

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest authenticationRequest) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> user = userService.findByEmail(authenticationRequest.getEmail());

        if (user.isPresent()) {
            if (passwordEncoder.matches(authenticationRequest.getPassword(), user.get().getPassword())) {
                final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
                final String jwt = jwtUtil.generateToken(userDetails.getUsername());


                response.put("message", "Login successful");
                response.put("jwtToken", jwt);

                return ResponseEntity.ok().body(response);
            } else {
                response.put("error", "Invalid email or password");
                return ResponseEntity.status(401).body(response);
            }
        } else {
            response.put("error", "User not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("/loggeduser")
    public ResponseEntity<User> getLoggedInUser(){
        User loggedInUser = loggedInUserUtil.getCurrentUserDetails();
        return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
    }
}
