package com.example.demo.Controller;

import com.example.demo.Service.BookService;
import com.example.demo.entity.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBook(@RequestParam String title, @RequestParam String author) {
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
    public ResponseEntity<String> decreaseCopies(@RequestParam String title, @RequestParam String author) {
        boolean success = bookService.decrementAvailableCopies(title, author);
        if (success) {
            return ResponseEntity.ok("Copies decreased successfully");
        }
        return ResponseEntity.badRequest().body("Failed to decrease copies (not enough copies or book not found)");
    }

    @PatchMapping("/incrementCopies")
    public ResponseEntity<String> incrementCopies(@RequestParam String title, @RequestParam String author) {
        boolean success = bookService.incrementAvailableCopies(title, author);
        if (success) {
            return ResponseEntity.ok("Copies incremented successfully");
        }
        return ResponseEntity.badRequest().body("Failed to increment copies (book not found)");
    }


}
