package org.semesterbreak;

import javafx.scene.control.ListCell;
import javafx.scene.web.WebEngine;

import java.lang.ref.WeakReference;

public class CustomFlashcardCell extends ListCell<FlashcardBridge> {

    private FlashcardContainer flashcardContainer;
    private WebEngine engine;

    public CustomFlashcardCell() {
        flashcardContainer = new FlashcardContainer(null);
        engine = flashcardContainer.getWebView().getEngine();
    }

    @Override
    protected void updateItem(FlashcardBridge item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null){
            setGraphic(null);
            return;
        }

        Flashcard flashcard = item.getFlashcard();

        if(flashcardContainer.getFlashcard() == null) {
            flashcardContainer.setFlashcard(flashcard);
            engine.loadContent(flashcard.getHTMLContent());
            item.setWebView(new WeakReference<>(flashcardContainer.getWebView()));
        }

        if(!flashcardContainer.getFlashcard().equals(item.getFlashcard())) {
            String changedContent = (String) engine.executeScript("document.documentElement.outerHTML");
            flashcardContainer.getFlashcard().setHTMLContent(changedContent);
            item.setWebView(null);
            flashcardContainer.setFlashcard(flashcard);
            engine.loadContent(flashcard.getHTMLContent());
            item.setWebView(new WeakReference<>(flashcardContainer.getWebView()));
        }

        flashcardContainer.setSelected(isSelected());

        setGraphic(flashcardContainer.getWebViewContainer());
    }
}

