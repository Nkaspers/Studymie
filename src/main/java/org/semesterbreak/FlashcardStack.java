package org.semesterbreak;

import java.util.ArrayList;
import java.util.List;

public class FlashcardStack implements TreeViewElement{
    private List<Flashcard> flashcards;
    private String title;

    public FlashcardStack(String title) {
        this.title = title;
        flashcards = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    @Override
    public boolean isFlashcard() {
        return false;
    }
}
