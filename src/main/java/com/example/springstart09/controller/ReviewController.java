package com.example.springstart09.controller;

import com.example.springstart09.model.AnswerRequest;
import com.example.springstart09.model.AnswerResult;
import com.example.springstart09.model.Flashcard;
import com.example.springstart09.model.QuestionResponse;
import com.example.springstart09.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.example.springstart09.model.CreateFlashcardRequest;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<Flashcard> review() {
        return reviewService.getAllCards();
    }

    @GetMapping("/{id}")
    public Flashcard getCard(@PathVariable long id) {
        return reviewService.getCardById(id);
    }

    @PostMapping("/{id}/check")
    public AnswerResult checkAnswer(
            @PathVariable long id,
            @Valid @RequestBody AnswerRequest request) {

        return reviewService.checkAnswer(id, request.getAnswer());
    }

    @GetMapping("/count")
    public long getTotalCards() {
        return reviewService.getTotalCards();
    }

    @GetMapping("/search")
    public Flashcard search(@RequestParam String word) {
        return reviewService.findByWord(word);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Flashcard createCard(
            @Valid @RequestBody CreateFlashcardRequest request) {

        return reviewService.addCard(
                request.getWord(),
                request.getMeaning());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(@PathVariable long id) {
        reviewService.deleteCard(id);
    }

    @PutMapping("/{id}")
    public Flashcard updateCard(
            @PathVariable("id") long id,
            @Valid @RequestBody CreateFlashcardRequest request) {

        return reviewService.updateCard(
                id,
                request.getWord(),
                request.getMeaning());
    }

    @GetMapping("/filter")
    public List<Flashcard> filterCards(@RequestParam String keyword) {
        return reviewService.filterCards(keyword);
    }

    @GetMapping("/random")
    public Flashcard getRandomCard() {
        return reviewService.getRandomCard();
    }

    @GetMapping("/{id}/question") // method to get a question for a specific flashcard id
    public QuestionResponse getQuestion(@PathVariable long id) {
        return reviewService.getQuestion(id);
    }

    @GetMapping("/random/question")
    public QuestionResponse getRandomQuestion() {
        return reviewService.getRandomQuestion();
    }

}