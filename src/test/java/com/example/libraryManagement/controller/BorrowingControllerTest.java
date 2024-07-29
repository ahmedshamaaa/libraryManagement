package com.example.libraryManagement.controller;

import com.example.libraryManagement.entity.Book;
import com.example.libraryManagement.entity.BorrowingRecord;
import com.example.libraryManagement.entity.Patron;
import com.example.libraryManagement.repository.BookRepository;
import com.example.libraryManagement.repository.BorrowingRecordRepository;
import com.example.libraryManagement.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BorrowingControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/api";
    }

    @Test
    public void testBorrowBook() {
        // Create and save a book
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Author");
        book.setIsbn("1234567890123");
        book.setPublicationYear(2024);
        Book savedBook = bookRepository.save(book);

        // Create and save a patron
        Patron patron = new Patron();
        patron.setName("Test Patron");
        patron.setContactInfo("test@example.com");
        Patron savedPatron = patronRepository.save(patron);

//        BorrowingRecord record = new BorrowingRecord();
//        record.setBook(book);
//        record.setPatron(patron);
//        record.setBorrowDate(LocalDate.now());
//        record.setReturnDate(LocalDate.now().plusDays(7)); // Set return date
//        BorrowingRecord borrowingRecord = borrowingRecordRepository.save(record);


        // Perform the borrow request
        String url = baseUrl() + "/borrow/" + savedBook.getId() + "/patron/" + savedPatron.getId();
        ResponseEntity<BorrowingRecord> response = restTemplate.postForEntity(url, null, BorrowingRecord.class);

        // Check the response
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify the borrowing record was created
        BorrowingRecord savedRecord = response.getBody();
        assertNotNull(savedRecord);
        assertEquals(savedBook.getId(), savedRecord.getBook().getId());
        assertEquals(savedPatron.getId(), savedRecord.getPatron().getId());
        assertEquals(LocalDate.now(), savedRecord.getBorrowDate());
        assertNotNull(savedRecord.getReturnDate()); // Ensure returnDate is set
    }


}
