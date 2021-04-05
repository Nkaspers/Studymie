package org.semesterbreak;
import javafx.scene.web.WebView;

public class FlashcardWebView {

    private final WebView webView;
    private Flashcard flashcard;

    public FlashcardWebView(Flashcard flashcard) {

        this.flashcard = flashcard;

        webView = new WebView();
    }
    public WebView getWebView() {
        return webView;
    }

    public Flashcard getFlashcard() {
            return flashcard;
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
    }
}


