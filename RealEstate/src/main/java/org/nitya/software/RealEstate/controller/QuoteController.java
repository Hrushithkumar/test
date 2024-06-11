package org.nitya.software.RealEstate.controller;

import org.nitya.software.RealEstate.dto.QuoteDto;
import org.nitya.software.RealEstate.model.Quote;
import org.nitya.software.RealEstate.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/quotes")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    @GetMapping
    public ResponseEntity<List<Quote>> getQuotes(){
        return new ResponseEntity<>(quoteService.getAllQuotes(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Quote> createQuote(@Valid  @RequestBody Quote quote){
        return new ResponseEntity<>(quoteService.createQuote(quote), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update/status")
    public ResponseEntity<Quote> updateStatus(@PathVariable Long id, @RequestBody Map<String,String> statusUpdate){
        String status = statusUpdate.get("status");
        Optional<Quote> updatedQuote = quoteService.updateStatus(id, status);
        return updatedQuote.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}/update/comment")
    public ResponseEntity<Quote> updateComment(@PathVariable Long id, @RequestBody Map<String,String> statusUpdate){
        String comment = statusUpdate.get("comment");
        Optional<Quote> updatedQuote = quoteService.updateComment(id, comment);
        return updatedQuote.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuote(@PathVariable Long id){
        quoteService.deleteQuoteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
