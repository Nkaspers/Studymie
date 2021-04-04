package org.semesterbreak;
import javafx.scene.web.WebView;

public class WebViewManager {

    public void makeUnderlined(WebView webView){webView.getEngine().executeScript("document.execCommand(\"underline\");");}

    public void makeItalic(WebView webView) { webView.getEngine().executeScript("document.execCommand(\"italic\");"); }

    public void makeBold(WebView webView) {webView.getEngine().executeScript("document.execCommand(\"bold\");");}

    public void leftAlign(WebView webView) {}


    public void blockAlign(WebView webView) {
    }

    public void rightAlign(WebView webView) {
    }

    public void addBulletList(WebView webView) {
        webView.getEngine().executeScript("document.execCommand(\"insertUnorderedList\");");
    }

    public void addNumberedList(WebView webView) {
        webView.getEngine().executeScript("document.execCommand(\"insertOrderedList\");");

    }
}
