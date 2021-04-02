package org.semesterbreak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlashcardStack implements TreeViewElement{
    public List<Flashcard> flashcards;
    public String title;

    public FlashcardStack(String s) {
        title = s;
    }

    public String getTitle() {
        return "Titel";
    }

    public List<Flashcard> getFlashcards() {
        return new ArrayList<>(Arrays.asList(new Flashcard("Frage"), new Flashcard("Frage2")));
    }

    @Override
    public boolean isFlashcard() {
        return false;
    }
}
