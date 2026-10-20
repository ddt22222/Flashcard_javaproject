package com.example.springstart09.service;

import com.example.springstart09.model.Flashcard;
import com.example.springstart09.model.QuestionResponse;
import com.example.springstart09.repository.FlashcardRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.springstart09.model.AnswerResult;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ReviewService {

    private final FlashcardRepository flashcardRepository;

    public ReviewService(FlashcardRepository flashcardRepository) {
        this.flashcardRepository = flashcardRepository;
    }

    public List<Flashcard> getAllCards() { // method to get all flashcards
        return flashcardRepository.findAll();
    }


    public AnswerResult checkAnswer(long id, String answer) { // method to check if the answer is correct
        if (answer == null || answer.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Đáp án không được để trống");
        }

        Flashcard card = getCardById(id);

        boolean correct = card.getMeaning()
                .equalsIgnoreCase(answer.trim());

        return new AnswerResult(
                correct,
                card.getMeaning(),
                card.getWord());
    }

    public long getTotalCards() {
        return flashcardRepository.count();
    }

    public Flashcard findByWord(String word) {
        if (word == null || word.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Từ tìm kiếm không được để trống");
        }

        return flashcardRepository.findByWordIgnoreCase(word.trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy từ"));
    }

    public Flashcard addCard(String word, String meaning) {
        String normalizedWord = word.trim();

        if (flashcardRepository.existsByWordIgnoreCase(normalizedWord)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Từ này đã tồn tại");
        }

        Flashcard card = new Flashcard(
                normalizedWord,
                meaning.trim());

        return flashcardRepository.save(card);
    }

    public void deleteCard(long id) {
        Flashcard card = getCardById(id);
        flashcardRepository.delete(card);
    }

    public Flashcard updateCard(long id, String word, String meaning) {
        Flashcard card = getCardById(id);
        String normalizedWord = word.trim();

        if (flashcardRepository.existsByWordIgnoreCaseAndIdNot(
                normalizedWord, id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Từ này đã tồn tại");
        }

        card.updateContent(normalizedWord, meaning.trim());

        return flashcardRepository.save(card);
    }

    public List<Flashcard> filterCards(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Từ khóa không được để trống");
        }

        return flashcardRepository
                .findByWordContainingIgnoreCase(keyword.trim());
    }

    public Flashcard getRandomCard() {
        List<Flashcard> storedCards = flashcardRepository.findAll();

        if (storedCards.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Chưa có thẻ để ôn tập");
        }

        int randomIndex = ThreadLocalRandom.current()
                .nextInt(storedCards.size());

        return storedCards.get(randomIndex);
    }

    public QuestionResponse getQuestion(long id) { // method to get a question for a specific flashcard id
        Flashcard card = getCardById(id);

        return new QuestionResponse(id, card.getWord());
    }

    public QuestionResponse getRandomQuestion() {
        Flashcard card = getRandomCard();

        return new QuestionResponse(card.getId(), card.getWord());
    }

    public Flashcard getCardById(long id) {
        return flashcardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy thẻ"));
    }

}