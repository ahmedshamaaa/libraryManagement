package com.example.libraryManagement.service;

import com.example.libraryManagement.entity.Patron;
import com.example.libraryManagement.exception.ResourceNotFoundException;
import com.example.libraryManagement.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class PatronService {
    @Autowired
    private PatronRepository patronRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "patronsList")
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "patrons", key = "#id")
    public Patron getPatronById(Long id) {
        return patronRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patron not found with id " + id));
    }

    @Transactional
    @CacheEvict(value = {"patrons", "patronsList"}, allEntries = true)
    public Patron addPatron(Patron patron) {
        return patronRepository.save(patron);
    }

    @Transactional
    @CacheEvict(value = {"patrons", "patronsList"}, allEntries = true)
    public Patron updatePatron(Long id, Patron patronDetails) {
        Patron patron = getPatronById(id);
        patron.setName(patronDetails.getName());
        patron.setContactInfo(patronDetails.getContactInfo());
        return patronRepository.save(patron);
    }


    @Transactional
    @CacheEvict(value = {"patrons", "patronsList"}, allEntries = true)

            public void deletePatron(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Patron not found with id " + id);
        }
        patronRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return patronRepository.existsById(id);
    }

}
