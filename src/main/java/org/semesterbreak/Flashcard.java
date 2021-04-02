package org.semesterbreak;

public class Flashcard implements TreeViewElement{
    public String question;
    public Flashcard(String question) {
        this.question = question;
    }

    @Override
    public boolean isFlashcard() {
        return true;
    }
}
