package org.semesterbreak;

import javafx.scene.web.WebView;

import java.lang.ref.WeakReference;

public class FlashcardBridge {
    private Flashcard flashcard;
    private WeakReference<WebView> webView;

    public FlashcardBridge(Flashcard flashcard){
        this.flashcard = flashcard;
    }

    public Flashcard getFlashcard() {
        return flashcard;
    }

    public void setWebView(WeakReference<WebView> webView) {
        this.webView = webView;
    }

    public WebView getWebView(){
        if(webView == null) return null;
        return webView.get();
    }
}
