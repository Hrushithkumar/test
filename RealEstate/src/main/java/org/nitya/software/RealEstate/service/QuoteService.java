package org.nitya.software.RealEstate.service;

import org.nitya.software.RealEstate.model.Quote;
import org.nitya.software.RealEstate.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    public List<Quote> getAllQuotes(){
        return quoteRepository.findAll();
    }

    public Optional<Quote> getQuoteById(Long id){
        Quote quote =  quoteRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No quote found with id" + id));
        return Optional.ofNullable(quote);
    }

    public Quote createQuote(Quote quote){
        return quoteRepository.save(quote);
    }

    public void deleteQuoteById(Long id){
        quoteRepository.deleteById(id);
    }

    public void deleteQuotes(){
        quoteRepository.deleteAll();
    }

    @Transactional
    public Optional<Quote> updateStatus(Long id, String status) {
        Optional<Quote> quoteOptional = quoteRepository.findById(id);
        if (quoteOptional.isPresent()) {
            Quote quote = quoteOptional.get();
            quote.setStatus(status);
            quoteRepository.save(quote);
        }
        return quoteOptional;
    }

    @Transactional
    public Optional<Quote> updateComment(Long id, String comment) {
        Optional<Quote> quoteOptional = quoteRepository.findById(id);
        if (quoteOptional.isPresent()) {
            Quote quote = quoteOptional.get();
            quote.setComment(comment);
            quoteRepository.save(quote);
        }
        return quoteOptional;
    }
}
