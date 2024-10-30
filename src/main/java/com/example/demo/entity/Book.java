package com.example.demo.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idBooks")
    private Long id;

    @NotBlank(message = "Author cannot be blank")
    @Column(name = "Author")  // No need for nullable = false here; handled by @NotBlank
    private String author;

    @NotBlank(message = "Title cannot be blank")
    @Column(name = "Title")
    private String title;

    @Column(name = "Description", length = 1000)
    private String description;

    @Min(value = 1, message = "Copies must be at least 1")
    @Column(name = "Copies", nullable = false)
    private int copies;

    @Min(value = 0, message = "Available copies cannot be negative")
    @Column(name = "Available Copies", nullable = false)
    private int availableCopies;

    @Column(name = "Category")
    private String category;

    @NotBlank(message = "Language cannot be blank")
    @Column(name = "Language")
    private String language;
}
