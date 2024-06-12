package org.nitya.software.RealEstate.repository;

import org.nitya.software.RealEstate.model.ContactForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactsRepository extends JpaRepository<ContactForm, Long> {
}
