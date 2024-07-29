package com.example.libraryManagement.controller;

import com.example.libraryManagement.entity.Book;
import com.example.libraryManagement.entity.Patron;
import com.example.libraryManagement.exception.BadRequestException;
import com.example.libraryManagement.exception.ResourceNotFoundException;
import com.example.libraryManagement.repository.BookRepository;
import com.example.libraryManagement.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookRepository bookRepository;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/api/books";
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPublicationYear(2024);
        book.setIsbn("1234567890123");
        bookRepository.save(book);

        // Act
        ResponseEntity<Book[]> response = restTemplate.getForEntity(baseUrl(), Book[].class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Book> books = List.of(response.getBody());
        assertTrue(books.size() > 0);
    }

    @Test
    void testGetBookById() {
        // Arrange
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPublicationYear(2024);
        book.setIsbn("1234567890123");
        book = bookRepository.save(book);

        // Act
        ResponseEntity<Book> response = restTemplate.getForEntity(baseUrl() + "/" + book.getId(), Book.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Book retrievedBook = response.getBody();
        assertNotNull(retrievedBook);
        assertEquals("Test Book", retrievedBook.getTitle());
    }

    @Test
    void testAddBook() {
        // Arrange
        Book book = new Book();
        book.setTitle("New Book");
        book.setAuthor("New Author");
        book.setPublicationYear(2024);
        book.setIsbn("9876543210987");
        HttpEntity<Book> request = new HttpEntity<>(book);

        // Act
        ResponseEntity<Book> response = restTemplate.postForEntity(baseUrl(), request, Book.class);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Book createdBook = response.getBody();
        assertNotNull(createdBook);
        assertEquals("New Book", createdBook.getTitle());
    }

    @Test
    void testUpdateBook() {
        // Arrange
        Book book = new Book();
        book.setTitle("Old Title");
        book.setAuthor("Old Author");
        book.setPublicationYear(2024);
        book.setIsbn("1234567890123");
        book = bookRepository.save(book);

        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setPublicationYear(2025);
        updatedBook.setIsbn("1234567890123");
        HttpEntity<Book> request = new HttpEntity<>(updatedBook);

        // Act
        restTemplate.put(baseUrl() + "/" + book.getId(), request);
        ResponseEntity<Book> response = restTemplate.getForEntity(baseUrl() + "/" + book.getId(), Book.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Book resultBook = response.getBody();
        assertNotNull(resultBook);
        assertEquals("Updated Title", resultBook.getTitle());
    }



    @Test
    void testDeleteBook_NotFound() {
        // Arrange: Use an ID that does not exist
        String url = baseUrl() + "/99999";

        try {
            // Act: Perform the DELETE request
            restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
            // If no exception is thrown, the test should fail
            fail("Expected HttpClientErrorException to be thrown");
        } catch (HttpClientErrorException e) {
            // Assert: Check that the response status code is 404 Not Found
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode()); // Verify 404 status
        }
    }

    @Test
    void testDeleteBook (){
        // Arrange: Use an ID that does not exist

        Book book = new Book();
        book.setTitle("shamaa Book");
        book.setAuthor("shamaa Author");
        book.setPublicationYear(2024);
        book.setIsbn("1234567890123");
        Book savedBook = bookRepository.save(book);
        Long id = savedBook.getId();

        String url = baseUrl() + "/"+id;

        assertTrue(bookRepository.findById(id).isPresent());
        // Act: Perform the DELETE request
        restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
        assertFalse(bookRepository.findById(id).isPresent());


    }

    @Test
    void testDeleteBookNotFounded (){
        Book book = new Book();
        book.setTitle("shamaa Book");
        book.setAuthor("shamaa Author");
        book.setPublicationYear(2024);
        book.setIsbn("1234567890123");
        Book savedBook = bookRepository.save(book);
        Long id = savedBook.getId();
        String url = baseUrl() + "/"+id;

        restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

        try {
            // Act: Perform the DELETE request
            restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
            // If no exception is thrown, the test should fail
            fail("Expected HttpClientErrorException to be thrown");
        } catch (HttpClientErrorException e) {
            // Assert: Check that the response status code is 404 Not Found
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode()); // Verify 404 status
        }

    }








}
