package org.semesterbreak;

import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FlashcardManager {
    private final List<FlashcardStack> stackList;
    private String defaultFlashcardHTML;

    public FlashcardManager() {
        stackList = new ArrayList<>();

        try {
            defaultFlashcardHTML = Files.readString(Paths.get(getClass().getResource("Flashcard.html").toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        stackList.addAll(generateTestFlashcards());
    }

    private List<FlashcardStack> generateTestFlashcards() {
        int testStackCount = (int) (Math.random() * 7) + 4;
        List<FlashcardStack> list = new ArrayList<>();
        for(int i = 1; i<testStackCount; i++) {
            FlashcardStack stack = new FlashcardStack("Stapel " + (i+1));
            int testFlashcardCount = (int) (Math.random() * 10 + 3);
            for(int j = 0; j<testFlashcardCount; j++) {
                stack.getFlashcards().add(generateNewFlashcardWithRandomContent(stack));
            }
            list.add(stack);
        }
        return list;
    }

    private Flashcard generateNewFlashcardWithRandomContent(FlashcardStack stack) {
        String randomContentHTML = String.format(defaultFlashcardHTML, getClass().getResource("NotoSansHK-Regular.otf").toExternalForm(),
                lorem.substring(0, (int)(Math.random()*(lorem.length()-1) / 60)), lorem.substring((int)(Math.random()*(lorem.length()-1))));

        return new Flashcard(randomContentHTML, stack);
    }

    public List<FlashcardStack> getStackList(){
        return stackList;
    }

    public Flashcard addFlashcard(FlashcardStack stack) {
        String content = String.format(defaultFlashcardHTML, getClass().getResource("NotoSansHK-Regular.otf").toExternalForm(), "", "");

        Flashcard flashcard = new Flashcard(content, stack);
        stack.getFlashcards().add(flashcard);
        return flashcard;
    }

    public FlashcardStack addFlashcardStack() {
        FlashcardStack flashcardStack = new FlashcardStack("neuer Stapel");
        stackList.add(flashcardStack);
        return flashcardStack;
    }

    public void removeFromStack(Flashcard flashcard) {
        flashcard.getCurrentStack().getFlashcards().remove(flashcard);
    }

    public void removeStack(FlashcardStack stack) {
        getStackList().remove(stack);
    }

    public void moveFlashcard(Flashcard flashcard, int shift) {
        int index = flashcard.getCurrentStack().getFlashcards().indexOf(flashcard);
        flashcard.getCurrentStack().getFlashcards().remove(flashcard);
        flashcard.getCurrentStack().getFlashcards().add(index+shift, flashcard);
    }

    public void moveFlashcardStack(FlashcardStack stack, int shift) {
        int index = stackList.indexOf(stack);
        stackList.remove(stack);
        stackList.add((index+shift), stack);
    }

    public Flashcard duplicateFlashcard(Flashcard flashcard) {
            FlashcardStack stack = flashcard.getCurrentStack();
            int index = stack.getFlashcards().indexOf(flashcard);
            Flashcard flashcardCopy = flashcard.clone();
            stack.getFlashcards().add(index+1, flashcardCopy);
            return flashcardCopy;
    }

    private String lorem = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua." +
            " At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea " +
            "takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor " +
            "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea " +
            "takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna " +
            "aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.   \n" +
            "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim " +
            "qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt " +
            "ut laoreet dolore magna aliquam erat volutpat.   \n" +
            "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate " +
            "velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.   \n" +
            "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam " +
            "nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.   \n" +
            "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis.   \n" +
            "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur";
}
