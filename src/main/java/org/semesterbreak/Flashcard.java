package org.semesterbreak;

public class Flashcard implements TreeViewElement{
    private String question;
    private String htmlContent;

    private transient FlashcardStack currentStack;

    public Flashcard() {}

    public Flashcard(String htmlContent,  FlashcardStack stack) {
        this.htmlContent = htmlContent;
        this.currentStack = stack;
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

    public FlashcardStack getCurrentStack() {
        return currentStack;
    }

    public void setCurrentStack(FlashcardStack currentStack) {
        this.currentStack = currentStack;
    }

    public String getHTMLContent() {
        return htmlContent;
    }

    public void setHTMLContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Flashcard clone() {
        Flashcard flashcard = new Flashcard();
        flashcard.setQuestion(this.getQuestion());
        flashcard.setHTMLContent(this.getHTMLContent());
        flashcard.setCurrentStack(this.getCurrentStack());
        return flashcard;
    }
}
