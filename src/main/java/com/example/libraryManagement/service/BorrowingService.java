package com.example.libraryManagement.service;

import com.example.libraryManagement.entity.Book;
import com.example.libraryManagement.entity.BorrowingRecord;
import com.example.libraryManagement.entity.Patron;
import com.example.libraryManagement.repository.BookRepository;
import com.example.libraryManagement.repository.BorrowingRecordRepository;
import com.example.libraryManagement.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class BorrowingService {
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Transactional
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new RuntimeException("Patron not found"));

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setBorrowDate(LocalDate.now());
        record.setReturnDate(LocalDate.now().plusDays(14));
        return borrowingRecordRepository.save(record);
    }


}