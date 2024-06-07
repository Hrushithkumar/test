package org.nitya.software.RealEstate.utils;

import org.nitya.software.RealEstate.model.Role;
import org.nitya.software.RealEstate.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("ROLE_USER") == null) {
            Role role = new Role();
            role.setName("ROLE_USER");
            roleRepository.save(role);
        }

        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            roleRepository.save(role);
        }

        if (roleRepository.findByName("ROLE_EMPLOYEE") == null) {
            Role role = new Role();
            role.setName("ROLE_EMPLOYEE");
            roleRepository.save(role);
        }
    }
}
