package com.example.demo.Service;

import com.example.demo.Repository.BookRepository;
import com.example.demo.entity.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public boolean deleteBookById(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean updateBookById(Long id, Book book) {
        if (bookRepository.existsById(id)) {
            book.setId(id);
            bookRepository.save(book);
            return true;
        } else {
            return false;
        }
    }

}
