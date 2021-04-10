package org.semesterbreak.scenes.editor;


import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.jdom2.JDOMException;
import javafx.animation.TranslateTransition;
import org.semesterbreak.Flashcard;
import org.semesterbreak.FlashcardManager;
import org.semesterbreak.FlashcardStack;
import org.semesterbreak.Utilities;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class EmptyStackViewController {

    @FXML
    private Button expandButton;

    @FXML
    private AnchorPane navList;

    @FXML
    private Button projectButton;

    @FXML
    private Button exportButton;

    @FXML
    private VBox MenuVBox;

    @FXML
    private Label textLabel;

    @FXML
    private Button editButton;

    @FXML
    private ListView<FlashcardStack> stacksListView;

    @FXML
    private Label spacerLabel;
    @FXML
    private WebView questionWebView;
    private WebEngine engine = new WebEngine();
    @FXML
    private Button answerButton;



    private FlashcardManager flashcardManager = new FlashcardManager();

    @FXML
    void projectBtn(ActionEvent event) {

    }

    public void initialize(){
        try{
            expandButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/stack_icon.svg"));
            exportButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/export.svg"));
            editButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/edit.svg"));
            textLabel.setAlignment(Pos.CENTER);
            //answerButton.setAlignment(Pos.CENTER);
        }

        catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        prepareSlide();

        stacksListView.setItems(prepareListView());
        Flashcard testCard = stacksListView.getItems().get(0).getFlashcards().get(0);
        stacksListView.setCellFactory(stackListView -> new StackListCell());
        stacksListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        showQuestion(testCard);
    }



    private void prepareSlide(){

        AtomicBoolean navClosed = new AtomicBoolean(true);
        TranslateTransition openNav=new TranslateTransition(new Duration(350), navList);
        openNav.setByX(300);
        TranslateTransition closeNav=new TranslateTransition(new Duration(350), navList);


        expandButton.setOnAction((ActionEvent evt)->{
            if(navClosed.get()){
                //to right transition
                openNav.play();
                navClosed.set(false);
            }else{
                //to left transition
                closeNav.setByX(-MenuVBox.getWidth());
                closeNav.play();
                navClosed.set(true);
            }
        });

    }

    private ObservableList<FlashcardStack> prepareListView(){
        ObservableList<FlashcardStack> data = FXCollections.observableArrayList();
        data.addAll(flashcardManager.getStackList());

        return data;
    }

    private void showQuestion(Flashcard flashcard){
        engine = questionWebView.getEngine();
        engine.loadContent(flashcard.getHTMLContent(), "text/html");
    }
}


