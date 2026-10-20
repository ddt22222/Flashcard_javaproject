package com.example.springstart09.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "flashcards")
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String word;

    @Column(nullable = false, length = 200)
    private String meaning;

    protected Flashcard() {
    }

    public Flashcard(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public void updateContent(String word, String meaning) {
    this.word = word;
    this.meaning = meaning;
}

    public Long getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }
}