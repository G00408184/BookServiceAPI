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

    public Optional<Book> getBookByTitleAndAuthor(String title, String author) {
        return bookRepository.findBookByTitleAndAuthor(title, author);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public boolean deleteBook(String title, String author) {
        Optional<Book> book = bookRepository.findBookByTitleAndAuthor(title, author);
        if (bookRepository.findBookByTitleAndAuthor(title, author).isPresent()) {
            bookRepository.deleteById(book.get().getId());
            return true;
        }

        return false;
    }

    public boolean updateBookById(Long id, Book book) {
        if (bookRepository.existsById(id)) {
            book.setId(id);
            bookRepository.save(book);
            return true;
        }
        return false;
    }

    public boolean updateBookByTitleAndAuthor(String title, String author, Book updatedBook) {
        Optional<Book> book = bookRepository.findBookByTitleAndAuthor(title, author);
        if (book.isPresent()) {
            Book bookToUpdate = book.get();
            updatedBook.setId(bookToUpdate.getId());
            bookRepository.save(updatedBook);
            return true;
        }
        return false;
    }

    public String getBookInfo(String title, String author, String infoType) {
        Optional<Book> book = bookRepository.findBookByTitleAndAuthor(title, author);

        if (book.isPresent()) {
            switch (infoType.toLowerCase()) {
                case "available_copies":
                    return String.valueOf(book.get().getAvailableCopies());
                case "copies":
                    return String.valueOf(book.get().getCopies());
                case "description":
                    return book.get().getDescription();
                case "category":
                    return book.get().getCategory();
                case "language":
                    return book.get().getLanguage();
                default:
                    return "Invalid information type";
            }
        }
        return "Book not found";
    }

    public boolean decrementAvailableCopies(String title, String author) {
        Optional<Book> book = bookRepository.findBookByTitleAndAuthor(title, author);
        if (book.isPresent()) {
            Book bookToUpdate = book.get();
            if (bookToUpdate.getAvailableCopies() > 0) {
                bookToUpdate.setAvailableCopies(bookToUpdate.getAvailableCopies() - 1);
                bookRepository.save(bookToUpdate);
                return true; // Successfully decremented available copies
            }
        }
        return false; // Not enough copies available or book not found
    }

    public boolean incrementAvailableCopies(String title, String author) {
        Optional<Book> book = bookRepository.findBookByTitleAndAuthor(title, author);
        if (book.isPresent()) {
            Book bookToUpdate = book.get();
            bookToUpdate.setAvailableCopies(bookToUpdate.getAvailableCopies() + 1);
            bookRepository.save(bookToUpdate);
            return true; // Successfully incremented available copies
        }
        return false; // Book not found
    }
}
