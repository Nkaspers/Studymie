package org.semesterbreak;

import javafx.scene.web.WebView;
import netscape.javascript.JSException;


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

}