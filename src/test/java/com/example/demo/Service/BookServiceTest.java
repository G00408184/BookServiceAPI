package com.example.demo.Service;

import com.example.demo.Repository.BookRepository;
import com.example.demo.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getBookById() {
        Long id = 1L;
        Book book = new Book();
        book.setId(id);
        book.setTitle("Harry Potter");

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.getBookById(id);

        assertTrue(foundBook.isPresent());
        assertEquals("Harry Potter", foundBook.get().getTitle());
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    void getBookByTitleAndAuthor() {
        String title = "Harry Potter";
        String author = "J.K. Rowling";

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);

        when(bookRepository.findBookByTitleAndAuthor(title, author)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.getBookByTitleAndAuthor(title, author);

        assertTrue(foundBook.isPresent());
        assertEquals(author, foundBook.get().getAuthor());
        verify(bookRepository, times(1)).findBookByTitleAndAuthor(title, author);
    }

    @Test
    void getAllBooks() {
        bookService.getAllBooks();
        verify(bookRepository, times(1)).findAll();
    }


    @Test
    void updateBookById() {
        Long id = 1L;
        Book book = new Book();
        book.setId(id);
        book.setTitle("Harry Potter");

        when(bookRepository.existsById(id)).thenReturn(true);
        when(bookRepository.save(book)).thenReturn(book);

        boolean isUpdated = bookService.updateBookById(id, book);

        assertTrue(isUpdated);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void getBookInfo() {
        String title = "Harry Potter";
        String author = "J.K. Rowling";

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription("Fantasy book");

        when(bookRepository.findBookByTitleAndAuthor(title, author)).thenReturn(Optional.of(book));

        String info = bookService.getBookInfo(title, author, "description");

        assertEquals("Fantasy book", info);
        verify(bookRepository, times(1)).findBookByTitleAndAuthor(title, author);
    }

}
