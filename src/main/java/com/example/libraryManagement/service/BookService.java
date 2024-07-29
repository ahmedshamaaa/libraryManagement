

package com.example.libraryManagement.service;

        import com.example.libraryManagement.entity.Book;
        import com.example.libraryManagement.exception.ResourceNotFoundException;
        import org.springframework.cache.annotation.Cacheable;
        import com.example.libraryManagement.repository.BookRepository;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import org.springframework.transaction.annotation.Transactional;

        import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Cacheable("books")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
    }

    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }

    @Transactional
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id);
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setIsbn(bookDetails.getIsbn());
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);
    }


}