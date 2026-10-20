package com.example.springstart09.model;

public class QuestionResponse {
    private final long id;
    private final String word;

    public QuestionResponse(long id, String word) {
        this.id = id;
        this.word = word;
    }

    public long getId() {
        return id;
    }

    public String getWord() {
        return word;
    }
}