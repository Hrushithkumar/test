package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.model.User;
import org.nitya.software.RealEstate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Email address not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            // Generate/reset password reset token and save to user object
            String token = UUID.randomUUID().toString();
            User saUser = user.get();
            saUser.setResetToken(token);
            userRepository.save(saUser);

            // Send email with reset link
            sendResetEmail(saUser.getEmail(), token);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password reset email sent successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error occurred while processing forgot password request.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");

        Optional<User> user = userRepository.findByResetToken(token);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid token.");
        }

        try {
            User updatedUser = user.get();
            // Update user's password (ensure to hash the password)
            updatedUser.setPassword(passwordEncoder.encode(newPassword));
            updatedUser.setResetToken(null); // Invalidate the token after reset
            userRepository.save(updatedUser);

            return ResponseEntity.ok("Password successfully reset.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while processing the reset password request.");
        }
    }

    private void sendResetEmail(String email, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Reset your password");
        mailMessage.setText("To reset your password, click here: "
                + "http://localhost:8080/reset-password.html?token=" + token);
        javaMailSender.send(mailMessage);
    }
}
