package org.semesterbreak;

public class Flashcard implements TreeViewElement{
    private String question;
    public Flashcard(String question) {
        this.question = question;
    }

    @Override
    public boolean isFlashcard() {
        return true;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
