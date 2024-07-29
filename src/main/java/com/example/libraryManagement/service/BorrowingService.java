package com.example.libraryManagement.service;

import com.example.libraryManagement.entity.Book;
import com.example.libraryManagement.entity.BorrowingRecord;
import com.example.libraryManagement.entity.Patron;
import com.example.libraryManagement.repository.BookRepository;
import com.example.libraryManagement.repository.BorrowingRecordRepository;
import com.example.libraryManagement.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingService {
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Transactional
    @CacheEvict(value = {"books", "booksList","borrowingRecords"}, allEntries = true)
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        Integer bookCount = book.getCount();
        if (bookCount - 1 <= 0) {
            throw new RuntimeException("noo Book available");
        }
        Patron patron = patronRepository.findById(patronId).orElseThrow(() -> new RuntimeException("Patron not found"));

        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setBorrowDate(LocalDate.now());
        record.setReturnDate(LocalDate.now().plusDays(14));

        book.setCount(bookCount - 1);
        bookRepository.save(book);
        return borrowingRecordRepository.save(record);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "borrowingRecords")
    public List<BorrowingRecord> getAllBorrowingRecord() {
        return borrowingRecordRepository.findAll();
    }


}