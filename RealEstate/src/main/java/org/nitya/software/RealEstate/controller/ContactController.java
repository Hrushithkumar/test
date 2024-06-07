//package org.nitya.software.RealEstate.controller;
//
//import org.nitya.software.RealEstate.model.ContactForm;
//import org.nitya.software.RealEstate.service.EmailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api")
//public class ContactController {
//
//    @Autowired
//    private EmailService emailService;
//
//    @PostMapping("/contact")
//    public String sendContactEmail(@RequestBody ContactForm contactForm) {
//        String subject = "Contact Form Submission from " + contactForm.getName();
//        String message = "Name: " + contactForm.getName() + "\n"
//                + "Email: " + contactForm.getEmail() + "\n"
//                + "Message: " + contactForm.getMessage();
//        emailService.sendEmail("suchithbalne@gmail.com", subject, message);
//        return "Email sent successfully";
//    }
//}
