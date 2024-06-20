package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.model.ContactForm;
import org.nitya.software.RealEstate.service.ContactUsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class ContactController {

//    @Autowired
//    private EmailService emailService;

    @Autowired
    private ContactUsServices contactUsServices;

//    @PostMapping("/contact")
//    public String sendContactEmail(@RequestBody ContactForm contactForm) {
//        String subject = "Contact Form Submission from " + contactForm.getName();
//        String message = "Name: " + contactForm.getName() + "\n"
//                + "Email: " + contactForm.getEmail() + "\n"
//                + "Message: " + contactForm.getMessage();
//        emailService.sendEmail("suchithbalne@gmail.com", subject, message);
//        return "Email sent successfully";
//    }

    @GetMapping("/contacts")
    public ResponseEntity<List<ContactForm>> getAllContactsUsDetails(){
        List<ContactForm> contactFormsList = contactUsServices.getContactFormDetails();
        return new ResponseEntity<>(contactFormsList, HttpStatus.OK);
    }

    @PostMapping("/contacts")
    public ResponseEntity<ContactForm> createContactsUsRequest(@RequestBody ContactForm contactForm){
        ContactForm savedContactForm = contactUsServices.createContactForm(contactForm);
        return new ResponseEntity<>(savedContactForm, HttpStatus.CREATED);
    }
}
