package com.example.demo.Controller;

import com.example.demo.MessageQueue.Message;
import com.example.demo.MessageQueue.MessageProducer;
import com.example.demo.Service.BookService;
import com.example.demo.entity.Book;
import com.example.demo.entity.Loan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/TitleAuthor")
    public ResponseEntity<Optional<Book>> getBookByTitleAndAuthor(@RequestParam String title, @RequestParam String author) {
        Optional<Book> book = bookService.getBookByTitleAndAuthor(title, author);
        return ResponseEntity.ok(book);
    }
    
    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        // Ask for Admin permission
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }
    @PostMapping("/addGranted/{id}")
    public void addBookGranted(@PathVariable String id) {
       int Bookid = Integer.parseInt(id);

        // Ask for Admin permission
        bookService.createBookGranted(Bookid);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBook(@RequestParam String title, @RequestParam String author) {
        // Check if all the loans are completed

        if (bookService.deleteBook(title, author)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Book> editBook(@PathVariable Long id, @RequestBody Book book) {
        boolean isUpdated = bookService.updateBookById(id, book);
        if (isUpdated) {
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/edit")
    public ResponseEntity<Book> editBookByTitleAndAuthor(@RequestParam String title, @RequestParam String author, @RequestBody Book book) {
        boolean isUpdated = bookService.updateBookByTitleAndAuthor(title, author, book);
        if (isUpdated) {
            return ResponseEntity.ok(book);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/info")
    public ResponseEntity<String> getBookInfo(@RequestParam String title, @RequestParam String author, @RequestParam String infoType) {
        String info = bookService.getBookInfo(title, author, infoType);
        if ("Book not found".equals(info) || "Invalid information type".equals(info)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(info);
        }
        return ResponseEntity.ok(info);
    }

    @PatchMapping("/decreaseCopies")
    public boolean decreaseCopies(@RequestParam String title, @RequestParam String author) {
        return bookService.decrementAvailableCopies(title, author);
    }

    @PatchMapping("/incrementCopies")
    public boolean incrementCopies(@RequestParam String title, @RequestParam String author) {
       return bookService.incrementAvailableCopies(title, author);
    }

}
