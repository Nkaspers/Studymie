package org.semesterbreak;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.control.ListCell;
import javafx.scene.web.WebEngine;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.lang.ref.WeakReference;

public class CustomFlashcardCell extends ListCell<FlashcardBridge> {

    private FlashcardContainer flashcardContainer;
    private WebEngine engine;

    public CustomFlashcardCell() {
        flashcardContainer = new FlashcardContainer(null);
        engine = flashcardContainer.getWebView().getEngine();

        engine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if(newValue == Worker.State.SUCCEEDED) {
                    Element documentElement = engine.getDocument().getDocumentElement();
                    ((EventTarget) documentElement).addEventListener("input", new EventListener() {
                        @Override
                        public void handleEvent(Event evt) {
                            if(flashcardContainer.getFlashcard() == null) return;
                            flashcardContainer.getFlashcard().setHTMLContent((String) engine.executeScript("document.documentElement.outerHTML"));
                            String newQuestion = (String) engine.executeScript("document.getElementById('question').innerText");
                            flashcardContainer.getFlashcard().setQuestion(newQuestion);
                            System.out.println(Math.random());
                        }
                    }, false);
                }
            }
        });

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
            flashcardContainer.setFlashcard(flashcard);
            engine.loadContent(flashcard.getHTMLContent());
            item.setWebView(new WeakReference<>(flashcardContainer.getWebView()));
        }

        if(item.getWebView() == null) item.setWebView(new WeakReference<>(flashcardContainer.getWebView()));

        flashcardContainer.setSelected(isSelected());

        setGraphic(flashcardContainer.getWebViewContainer());
    }
}

