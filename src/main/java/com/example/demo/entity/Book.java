package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "copies", nullable = false)
    private int copies;

    @Column(name = "available_copies", nullable = false)
    private int availableCopies;

    @Column(name = "category")
    private String category;


    @Column(name = "language", nullable = false)
    private String language;
}
