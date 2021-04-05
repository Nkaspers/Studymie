package org.semesterbreak;

import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FlashcardPane extends AnchorPane{

    private final WebView webView;
    private Flashcard flashcard;

    public FlashcardPane(Flashcard flashcard) {
        setPrefHeight(500);
        setPrefWidth(800);
        getStyleClass().add("flash-card");

        this.flashcard = flashcard;

        webView = new WebView();
        webView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.SPACE) {
                    keyEvent.consume();
                }
            }
        });

        this.getChildren().add(webView);

        try {
            String doc = Files.readString(Paths.get(getClass().getResource("Flashcard.html").getPath()));
            doc = String.format(doc, getClass().getResource("NotoSansHK-Regular.otf").toExternalForm());

            webView.getEngine().loadContent(doc);

        } catch (IOException e) {
            e.printStackTrace();
        }

        setTopAnchor(webView, 0d);
        setBottomAnchor(webView, 0d);
        setLeftAnchor(webView, 0d);
        setRightAnchor(webView, 0d);
    }

    public void setSelected(boolean state) {
        pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), state);
    }

    public WebView getWebView() {
        return webView;
    }

    public void setWebViewOnMousePressedListener(EventHandler<MouseEvent> eventHandler) {
        webView.setOnMousePressed(eventHandler);
    }

    public Flashcard getFlashcard() {
            return flashcard;
    }
}


