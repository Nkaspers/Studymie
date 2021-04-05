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

        if (empty || item == null){
            setGraphic(null);
            return;
        }

        if(flashcardWebView.getFlashcard() == null) {
            flashcardWebView.setFlashcard(item);
            flashcardWebView.getWebView().getEngine().loadContent(item.getHTMLContent());
        }

        if(!flashcardWebView.getFlashcard().equals(item)) {
            String changedContent = (String) flashcardWebView.getWebView().getEngine().executeScript("document.documentElement.outerHTML");
            flashcardWebView.getFlashcard().setHTMLContent(changedContent);
            flashcardWebView.setFlashcard(item);
            flashcardWebView.getWebView().getEngine().loadContent(item.getHTMLContent());
        }

        flashcardWebView.setSelected(isSelected());
        setGraphic(flashcardWebView.getWebViewContainer());
    }

    public FlashcardWebView getFlashcardWebView() {
        return flashcardWebView;
    }
}

