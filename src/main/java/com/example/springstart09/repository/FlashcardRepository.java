package com.example.springstart09.repository;

import com.example.springstart09.model.Flashcard;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashcardRepository
        extends JpaRepository<Flashcard, Long> {

    boolean existsByWordIgnoreCase(String word);

    boolean existsByWordIgnoreCaseAndIdNot(String word, Long id);

    Optional<Flashcard> findByWordIgnoreCase(String word);

    List<Flashcard> findByWordContainingIgnoreCase(String keyword);

    List<Flashcard> findByWordContainingIgnoreCaseOrMeaningContainingIgnoreCase(
        String wordKeyword,
        String meaningKeyword);

}