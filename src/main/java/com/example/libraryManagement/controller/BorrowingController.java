package com.example.libraryManagement.controller;


import com.example.libraryManagement.entity.BorrowingRecord;
import com.example.libraryManagement.exception.ResourceNotFoundException;
import com.example.libraryManagement.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BorrowingController {
    @Autowired
    private BorrowingService borrowingService;

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecord> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        BorrowingRecord record = borrowingService.borrowBook(bookId, patronId);
        if (record == null) {
            throw new ResourceNotFoundException("Failed to borrow book with id " + bookId + " for patron with id " + patronId);
        }
        return ResponseEntity.ok(record);
    }

}