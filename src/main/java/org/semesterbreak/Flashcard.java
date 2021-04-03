package org.semesterbreak;

public class Flashcard implements TreeViewElement{
    private String question;
    private FlashcardStack currentStack;
    public Flashcard(String question, FlashcardStack stack) {
        this.question = question;
        this.currentStack = stack;
    }

    @Override

    public boolean isFlashcard() {
        return true;
    }

    public FlashcardStack getCurrentStack() {
        return currentStack;
    }

    public void setCurrentStack(FlashcardStack currentStack) {
        this.currentStack = currentStack;
    }

    public String getQuestion() {
        return question;
    }
}
