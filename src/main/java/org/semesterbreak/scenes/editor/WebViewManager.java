package org.semesterbreak.scenes.editor;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;
import org.semesterbreak.Flashcard;

import java.util.Arrays;


public class WebViewManager {

    public void makeUnderlined(WebView webView) {
        webView.getEngine().executeScript("document.execCommand(\"underline\");");
    }

    public void makeItalic(WebView webView) {
        webView.getEngine().executeScript("document.execCommand(\"italic\");");
    }

    public void makeBold(WebView webView) {
        webView.getEngine().executeScript("document.execCommand(\"bold\");");
    }

    public void leftAlign(WebView webView) {
        webView.getEngine().executeScript("document.execCommand(\"justifyLeft\");");
    }

    public void blockAlign(WebView webView) {
        webView.getEngine().executeScript("document.execCommand(\"justifyCenter\");");
    }

    public void rightAlign(WebView webView) {
        webView.getEngine().executeScript("document.execCommand(\"justifyRight\");");
    }

    public void addBulletList(WebView webView) {
        webView.getEngine().executeScript("document.execCommand(\"insertUnorderedList\");");
    }

    public void addNumberedList(WebView webView) {
        webView.getEngine().executeScript("document.execCommand(\"insertOrderedList\");");
    }


    public void changeFontColor(WebView webView, String color) {
        webView.getEngine().executeScript("document.execCommand(\"foreColor\", false, \"" + color + "\");");
    }

    public void changeFontSize(WebView webView, int size) {
        webView.getEngine().executeScript("document.execCommand(\"fontSize\", false, \"" + size + "\");");
    }

    public void changeFontType(WebView webView, String fontName) {
        webView.getEngine().executeScript("document.execCommand(\"fontName\", false, \"" + fontName + "\");");
    }

    public String getQuestion(WebView webView) {
        try {
            return (String) webView.getEngine().executeScript("document.getElementById('question').innerText");
        } catch (JSException e) {
            return null;
        }
    }

    public int getSelectionFontSize(WebView webView){
        int size = Integer.parseInt(webView.getEngine()
                .executeScript("window.getComputedStyle(window.getSelection().anchorNode.parentElement, null).getPropertyValue('font-size');")
                .toString().replace("px", ""));
        switch (size) {
            case 10: return 1;
            case 13: return 2;
            case 16: return 3;
            case 18: return 4;
            case 24: return 5;
            case 32: return 6;
            case 48: return 7;
            default: return 0;
            }
    }

    public Color getSelectionFontColor(WebView webView) {
        String colorString = (String) webView.getEngine()
                .executeScript("window.getComputedStyle(window.getSelection().anchorNode.parentElement, null).getPropertyValue('color');");
        String[] rgb = colorString.substring(4, colorString.length()-1).replace(" ", "").split(",");
        return Color.rgb(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2]));
    }

    public String getSelectionFontType(WebView webView) throws JSException {
        return (String) webView.getEngine()
                .executeScript("window.getComputedStyle(window.getSelection().anchorNode.parentElement).getPropertyValue('font-family');");
    }

    public boolean noSelection(WebView webView){
        return (boolean) webView.getEngine()
                .executeScript("window.getSelection().anchorNode == null");
    }

    public void setQuestion(FlashcardBridge selectedItem) {
        WebEngine engine;
        Flashcard flashcard = selectedItem.getFlashcard();

        if(selectedItem.getWebView() != null) {
            engine = selectedItem.getWebView().getEngine();
        }else {
            engine = new WebEngine();
            engine.loadContent(selectedItem.getFlashcard().getHTMLContent());
            engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue == Worker.State.SUCCEEDED) {
                    updateQuestion(engine, flashcard);
                }
            });
            return;
        }
        updateQuestion(engine, flashcard);
    }

    private void updateQuestion(WebEngine engine, Flashcard flashcard) {
        engine.executeScript("document.getElementById('question').innerText = '" + flashcard.getQuestion() + "';");
        flashcard.setHTMLContent((String) engine.executeScript("document.documentElement.outerHTML"));
    }
}