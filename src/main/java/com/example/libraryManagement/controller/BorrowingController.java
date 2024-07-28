package com.example.libraryManagement.controller;


import com.example.libraryManagement.entity.BorrowingRecord;
import com.example.libraryManagement.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BorrowingController {
    @Autowired
    private BorrowingService borrowingService;



    @GetMapping("/borrowingRecords")
    public List<BorrowingRecord> getAllPatrons() {
        return borrowingService.getAllBorrowingRecords();
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public BorrowingRecord borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        return borrowingService.borrowBook(bookId, patronId);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public BorrowingRecord returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        return borrowingService.returnBook(bookId, patronId);
    }
}
