package org.nitya.software.RealEstate.repository;

import org.nitya.software.RealEstate.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);

    Optional<Employee> findByEmail(String email);
    
    Optional<Employee> findByPhoneNumber(String phoneNumber);

    Optional<Employee> findByLastName(String lastName);

    List<Employee> findByUsernameContainingIgnoreCase(String username);

}
