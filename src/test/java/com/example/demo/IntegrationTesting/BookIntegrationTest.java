package com.example.demo.IntegrationTesting;

import com.example.demo.Repository.BookRepository;
import com.example.demo.entity.Book;
import com.example.demo.MessageQueue.MessageProducer;
import com.example.demo.feign.BookClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @MockBean
    private MessageProducer messageProducer;

    @MockBean
    private BookClient bookClient;

    private Book testBook;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll(); // Clear the database before each test

        testBook = new Book();
        testBook.setTitle("Test Book");
        testBook.setAuthor("Author Name");
        testBook.setDescription("Description");
        testBook.setCopies(5);
        testBook.setAvailableCopies(5);
        testBook.setCategory("Test Category");
        testBook.setLanguage("English");

        bookRepository.save(testBook); // Save the test book to the H2 database
    }

    @Test
    void createBook_shouldPersistAndReturnBook() throws Exception {
        // Mock RabbitMQ behavior
        doNothing().when(messageProducer).sendMessage(any());

        Book book = new Book();
        book.setTitle("New Book");
        book.setAuthor("New Author");
        book.setDescription("A new description");
        book.setCopies(10);
        book.setAvailableCopies(10);
        book.setCategory("New Category");
        book.setLanguage("English");

        mockMvc.perform(post("/books/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.author").value("New Author"));

        // Verify the database
        List<Book> allBooks = bookRepository.findAll();
        assertEquals(2, allBooks.size()); // Includes the initially saved testBook
    }

    @Test
    void deleteBook_shouldRemoveBookFromDB() throws Exception {
        // Mock Feign client behavior
        when(bookClient.getLoanByTitleAndAuthor("Test Book", "Author Name")).thenReturn(Collections.emptyList());

        mockMvc.perform(delete("/books/delete")
                        .param("title", "Test Book")
                        .param("author", "Author Name"))
                .andExpect(status().isNoContent());

        // Verify the database
        List<Book> allBooks = bookRepository.findAll();
        assertEquals(0, allBooks.size());
    }

    @Test
    void shouldGetAllBooks() throws Exception {
        mockMvc.perform(get("/books/get")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }

    @Test
    void shouldGetBookById() throws Exception {
        mockMvc.perform(get("/books/get/" + testBook.getId())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }
}
