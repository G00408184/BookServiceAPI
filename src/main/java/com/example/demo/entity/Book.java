package com.example.demo.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Column(name = "id")  // Changed column name to "id"
    private Long id;

    @JsonProperty("author")
    @NotBlank(message = "Author cannot be blank")
    @Column(name = "author")
    private String author;

    @JsonProperty("title")
    @NotBlank(message = "Title cannot be blank")
    @Column(name = "title")
    private String title;

    @JsonProperty("description")
    @Column(name = "description", length = 1000)
    private String description;

    @JsonProperty("copies")
    @Min(value = 1, message = "Copies must be at least 1")
    @Column(name = "copies", nullable = false)
    private int copies;

    @JsonProperty("availableCopies")
    @Min(value = 0, message = "Available copies cannot be negative")
    @Column(name = "available_copies", nullable = false)
    private int availableCopies;

    @JsonProperty("category")
    @Column(name = "category")
    private String category;

    @JsonProperty("language")
    @NotBlank(message = "Language cannot be blank")
    @Column(name = "language")
    private String language;
}
