package org.semesterbreak;

import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlashcardManager {
    private List<FlashcardStack> stackList;

    public FlashcardManager() {
        stackList = new ArrayList<>();

        FlashcardStack stack1 = new FlashcardStack("Stapel 1");
        stack1.getFlashcards().add(new Flashcard("Frage 1"));
        stack1.getFlashcards().add(new Flashcard("Frage 2"));

        FlashcardStack stack2 = new FlashcardStack("Stapel 2");
        stack2.getFlashcards().add(new Flashcard("Frage 1"));
        stack2.getFlashcards().add(new Flashcard("Frage 3"));

        stackList.addAll(Arrays.asList(stack1, stack2));
    }

    public List<FlashcardStack> getStackList(){
        return stackList;
    }

    public Flashcard addFlashcard(FlashcardStack stack) {
        Flashcard flashcard = new Flashcard("untitled");
        stack.getFlashcards().add(flashcard);
        return flashcard;
    }

    public FlashcardStack addFlashcardStack() {
        FlashcardStack flashcardStack = new FlashcardStack("neuer Stapel");
        stackList.add(flashcardStack);
        return flashcardStack;
    }

    public void removeFromStack(FlashcardStack flashcardStack, Flashcard flashcard) {
        flashcardStack.getFlashcards().remove(flashcard);
    }

    public void removeStack(FlashcardStack lastSelectedTreeViewItem) {
        getStackList().remove(lastSelectedTreeViewItem);
    }
}
