package org.nitya.software.RealEstate.service;

import org.nitya.software.RealEstate.model.ContactForm;
import org.nitya.software.RealEstate.repository.ContactsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactUsServices {

    @Autowired
    private ContactsRepository contactsRepository;

    public List<ContactForm> getContactFormDetails(){
        return contactsRepository.findAll();
    }

    public ContactForm createContactForm(ContactForm contactForm){
        return contactsRepository.save(contactForm);
    }

    public ContactForm getContactById(Long id){
        return contactsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact form with provided Id is not present" + id));
    }

    public void deleteContactById(Long id){
        contactsRepository.deleteById(id);
    }

    public void deleteContacts(){
        contactsRepository.deleteAll();
    }
}
