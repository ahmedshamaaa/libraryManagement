package com.example.libraryManagement.controller;


import com.example.libraryManagement.entity.Book;
import com.example.libraryManagement.entity.Patron;
import com.example.libraryManagement.exception.ResourceNotFoundException;
import com.example.libraryManagement.service.PatronService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {
    @Autowired
    private PatronService patronService;


    @GetMapping
    public List<Patron> getAllPatrons() {
        return patronService.getAllPatrons();
    }



    @GetMapping("/{id}")
    public ResponseEntity<Patron> getPatronById(@PathVariable Long id) {
        try {
            Patron patron = patronService.getPatronById(id);
            return ResponseEntity.ok(patron);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // Return 404 if book is not found

        }
    }

    @PostMapping
    public ResponseEntity<Patron> addPatron(@Valid @RequestBody Patron patron) {
        Patron savedPatron = patronService.addPatron(patron);
        return new ResponseEntity<>(savedPatron, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patron> updatePatron(@PathVariable Long id, @Valid @RequestBody Patron patronDetails) {
        Patron updatedPatron = patronService.updatePatron(id, patronDetails);
        if (updatedPatron == null) {
            throw new ResourceNotFoundException("Patron not found with id " + id);
        }
        return ResponseEntity.ok(updatedPatron);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        if (!patronService.existsById(id)) {
            return ResponseEntity.notFound().build(); // Return 404 if book is not found
        }
        patronService.deletePatron(id);
        return ResponseEntity.noContent().build(); // Return 204 NO_CONTENT if successfully deleted
    }




}
