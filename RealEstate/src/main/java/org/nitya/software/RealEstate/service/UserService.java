package org.nitya.software.RealEstate.service;

import org.nitya.software.RealEstate.model.User;
import org.nitya.software.RealEstate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    
    public void deleteAll() {
        userRepository.deleteAll();
    }

    public User findByEmail(String email) {
        System.out.println("Given email:" + email);
        //System.out.println(userRepository.findByEmail(email));
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public Optional<User> findByLastName(String lastName) {
        return userRepository.findByLastName(lastName);
    }
}
