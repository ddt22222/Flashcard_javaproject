package com.example.springstart09.model;

public class AnswerResult {
    private boolean correct;
    private String correctAnswer;
    private String word;

    public AnswerResult(boolean correct, String correctAnswer, String word) { // constructor to initialize the fields   
        this.correct = correct;
        this.correctAnswer = correctAnswer;
        this.word = word;       
    }

    public boolean isCorrect() { // getter method for the 'correct' field
        return correct;
    }

    public String getCorrectAnswer() { // getter method for the 'correctAnswer' field
        return correctAnswer;
    }

    public String getWord() { // getter method for the 'word' field
        return word;
    }
}