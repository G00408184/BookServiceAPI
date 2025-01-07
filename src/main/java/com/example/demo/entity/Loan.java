package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("userId")
    @Column(name = "UserId")
    private int userId;

    @JsonProperty("bookTitle")
    @Column(name = "book_title")
    private String bookTitle;

    @JsonProperty("author")
    @Column(name = "author")
    private String author;

    @JsonProperty("checkoutDate")
    @Column(name = "checkoutDate")
    private LocalDate checkoutDate;

    @JsonProperty("expiredDate")
    @Column(name = "expiredDate")
    private LocalDate expiredDate;

    @JsonProperty("email")
    @Column(name = "email")
    private String email;

    @JsonProperty("extensionCount")
    @Column(name = "extensionCount")
    private int extensionCount;

    @JsonProperty("overdue")
    @Column(name = "overdue")
    private boolean overdue;

    @JsonProperty("status")
    @Column(name = "status")
    private String status;
}