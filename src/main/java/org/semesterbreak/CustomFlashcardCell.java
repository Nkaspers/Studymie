package org.semesterbreak;

import javafx.scene.control.ListCell;

public class CustomFlashcardCell extends ListCell<Flashcard>{

    FlashcardWebView flashcardWebView;

    public CustomFlashcardCell() {
        flashcardWebView = new FlashcardWebView(null);
    }

    @Override
    protected void updateItem(Flashcard item, boolean empty) {
        super.updateItem(item, empty);
        if (empty){
            setGraphic(null);
            return;
        }
        if(flashcardWebView.getFlashcard() == null) flashcardWebView.setFlashcard(item);
        if(!flashcardWebView.getFlashcard().equals(item)) {
            flashcardWebView.getWebView().getEngine().loadContent(item.getHTMLContent());
        }
        setGraphic(flashcardWebView.getWebView());
    }

    public FlashcardWebView getFlashcardWebView() {
        return flashcardWebView;
    }
}

