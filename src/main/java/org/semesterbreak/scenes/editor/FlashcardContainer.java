package org.semesterbreak.scenes.editor;
import javafx.css.PseudoClass;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import org.semesterbreak.Flashcard;

public class FlashcardContainer {

    private final WebView webView;
    private Flashcard flashcard;
    private AnchorPane container;

    public FlashcardContainer(Flashcard flashcard) {

        this.flashcard = flashcard;

        webView = new WebView();

        container = new AnchorPane();

        container.setMaxWidth(600);
        container.setPrefHeight(400);

        container.getStyleClass().add("flashcard");
        container.getChildren().add(webView);

        AnchorPane.setTopAnchor(webView, 0.0);
        AnchorPane.setRightAnchor(webView, 0.0);
        AnchorPane.setBottomAnchor(webView, 0.0);
        AnchorPane.setLeftAnchor(webView, 0.0);
    }
    public WebView getWebView() {
        return webView;
    }

    public Pane getWebViewContainer() {
        return container;
    }

    public Flashcard getFlashcard() {
            return flashcard;
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
    }

    public void setSelected(boolean state) {
        container.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), state);
    }
}


