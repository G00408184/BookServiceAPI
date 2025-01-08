package com.example.demo.Service;

import com.example.demo.MessageQueue.Message;
import com.example.demo.MessageQueue.MessageProducer;
import com.example.demo.Repository.BookRepository;
import com.example.demo.entity.Book;
import com.example.demo.feign.BookClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BookService {
    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final BookClient bookClient;

    @Autowired
     private final MessageProducer messageProducer;

    HashMap<Integer, Book> spare = new HashMap<>();

    public BookService(BookRepository bookRepository, BookClient bookClient,MessageProducer messageProducer ) {
        this.bookRepository = bookRepository;
        this.bookClient = bookClient;
        this.messageProducer = messageProducer;
    }

    public Book createBook(Book book) {
        // ask for admin permission
        Message message = new Message();
        message.setId(String.valueOf(System.currentTimeMillis()));
        message.setTimestamp(LocalDateTime.now().toString());
        message.setContent("Book");
        message.setEmail("doesnotmatter@gmail.com");
        int code = generateRandomNumber(1,100000);
        message.setType(String.valueOf(code));
        messageProducer.sendMessage(message);
        spare.put(code, book);
        createBookGranted(code);
        return book;
    }

    public void createBookGranted(int id) {
        if (spare.containsKey(id)) {
            Book book = spare.get(id);
            spare.remove(id);
            bookRepository.save(book);
        } else {
            System.out.println("Book with ID " + id + " not found in spare storage.");
        }
    }

    public int generateRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
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

        // Check if there are any loans for the book
        List<com.example.demo.entity.Loan> loans = bookClient.getLoanByTitleAndAuthor(title, author);

        if (loans != null && !loans.isEmpty()) {
            throw new RuntimeException("Cannot delete book: Active loans exist for this book.");
        }

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
