package org.semesterbreak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlashcardManager {
    public List<FlashcardStack> stackList = new ArrayList<>(Arrays.asList(new FlashcardStack("Stack 1"), new FlashcardStack("Stack 2")));

    public List<FlashcardStack> getStackList(){
        return stackList;
    }

    public Flashcard addFlashcard(FlashcardStack stack) {
        Flashcard flashcard = new Flashcard("untitled");
        stack.getFlashcards().add(flashcard);
        return flashcard;
    }

    public FlashcardStack addStack(){
        FlashcardStack stack = new FlashcardStack("Neuer Stapel");
        stackList.add(stack);
        return stack;
    }

}
