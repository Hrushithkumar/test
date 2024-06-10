package org.nitya.software.RealEstate.utils;

import org.nitya.software.RealEstate.model.User;
import org.nitya.software.RealEstate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class LoggedInUserUtil {

    @Autowired
    private UserRepository userRepository;

    public String getCurrentUserEmail(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    public User getCurrentUserDetails(){
        String useremail = getCurrentUserEmail();
        return userRepository.findByEmail(useremail)
                .orElseThrow(() -> new RuntimeException("User with "+ useremail + " not found" ));
    }

    public String getCurrentUsername(){
        String username = getCurrentUserEmail();
        return userRepository.findByEmail(username).get().getUsername();
    }
}
