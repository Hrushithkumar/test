package org.nitya.software.RealEstate.repository;

import org.nitya.software.RealEstate.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role ,Long> {

    Role findByName(String name);
}
