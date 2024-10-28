package com.example.demo.Controller;

import com.example.demo.Service.BookService;
import com.example.demo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/books") // Base URL for book-related APIs
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/get")
    public ResponseEntity<Book> createBook(Book book) {
        bookService.createBook(book);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable long id) {
        Optional<Book> book =  bookService.getBookById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        bookService.createBook(book);
        return ResponseEntity.ok(book);
    }

}