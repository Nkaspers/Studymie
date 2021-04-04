package org.semesterbreak;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FlashcardPane extends AnchorPane {
    public FlashcardPane() {
        setPrefHeight(350);
        setPrefWidth(500);
        getStyleClass().add("flash-card");

        WebView webView = new WebView();
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
}

