package com.example.libraryManagement.controller;

import com.example.libraryManagement.entity.Book;
import com.example.libraryManagement.exception.BadRequestException;
import com.example.libraryManagement.exception.ResourceNotFoundException;
import com.example.libraryManagement.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        try {
            Book book = bookService.getBookById(id);
            return ResponseEntity.ok(book); // Return 200 OK if book is found
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build(); // Return 404 if book is not found
        }
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        if (book.getTitle() == null || book.getAuthor() == null || book.getCount()  == null ){
            throw new BadRequestException("Title ,Count and Author are required");
        }
        Book savedBook = bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book bookDetails) {
        Book updatedBook = bookService.updateBook(id, bookDetails);
        if (updatedBook == null) {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (!bookService.existsById(id)) {
            return ResponseEntity.notFound().build(); // Return 404 if book is not found
        }
        bookService.deleteBook(id); // Proceed to delete if found
        return ResponseEntity.noContent().build(); // Return 204 NO_CONTENT if successfully deleted
    }


}