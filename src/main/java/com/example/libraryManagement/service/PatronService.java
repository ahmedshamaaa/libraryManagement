package com.example.libraryManagement.service;

import com.example.libraryManagement.entity.Patron;
import com.example.libraryManagement.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatronService {
    @Autowired
    private PatronRepository patronRepository;

    @Cacheable("patrons")

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }
    @Cacheable(value = "patrons", key = "#id")

    public Patron getPatronById(Long id) {
        return patronRepository.findById(id).orElseThrow(() -> new RuntimeException ("Patron not found"));
    }
    @Transactional
    public Patron addPatron(Patron patron) {
        return patronRepository.save(patron);
    }

    @Transactional
    public Patron updatePatron(Long id, Patron patronDetails) {
        Patron patron = getPatronById(id);
        patron.setName(patronDetails.getName());
        patron.setContactInfo(patronDetails.getContactInfo());
        return patronRepository.save(patron);
    }

    @Transactional
    public void deletePatron(Long id) {
        Patron patron = getPatronById(id);
        patronRepository.delete(patron);
    }
}
