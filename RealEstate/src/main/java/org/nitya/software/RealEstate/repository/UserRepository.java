package org.nitya.software.RealEstate.repository;

import org.nitya.software.RealEstate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByLastName(String lastName);
}
