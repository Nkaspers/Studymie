package org.semesterbreak;

import javafx.scene.web.WebView;

public class FlashcardBridge {
    private Flashcard flashcard;
    private WebView webView;

    public FlashcardBridge(Flashcard flashcard){
        this.flashcard = flashcard;
    }

    public Flashcard getFlashcard() {
        return flashcard;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    public WebView getWebView(){
        return webView;
    }
}
