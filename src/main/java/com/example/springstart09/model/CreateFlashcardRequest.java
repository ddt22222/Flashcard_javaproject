package com.example.springstart09.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateFlashcardRequest {

    @NotBlank(message = "Từ không được để trống")
    @Size(max = 50, message = "Từ không được vượt quá 50 ký tự")
    private String word;

    @NotBlank(message = "Nghĩa không được để trống")
    @Size(max = 200, message = "Nghĩa không được vượt quá 200 ký tự")
    private String meaning;

    public CreateFlashcardRequest() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}