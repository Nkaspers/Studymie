package org.semesterbreak.scenes.playmode;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
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
import org.semesterbreak.scenes.editor.WebViewManager;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlaymodeController {

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
    private VBox playVBox;

    @FXML
    private Label spacerLabel;
    @FXML
    private WebView questionWebView;
    private WebEngine engine = new WebEngine();
    private final WebViewManager manager = new WebViewManager();
    @FXML
    private Button answerButton;

    @FXML Button nextButton;


    private FlashcardManager flashcardManager = new FlashcardManager();
    private FlashcardStack currentFlashCardStack;
    private Flashcard currentFlashcard;
    private int currentFlashcardIndex = -1;

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

        initializeListView();

        initiazeCurrent();
    }

    private void initiazeCurrent(){
        if(!stacksListView.getItems().isEmpty()){
            currentFlashCardStack = stacksListView.getItems().get(0);
            stacksListView.getSelectionModel().select(0);
            if(!currentFlashCardStack.getFlashcards().isEmpty()){
                currentFlashcardIndex = 0;
                currentFlashcard = currentFlashCardStack.getFlashcards().get(currentFlashcardIndex);
                showQuestion(currentFlashcard);
            }
        } else {
            playVBox.setVisible(false);
        }

        nextButton.setVisible(false);
    }

    private void initializeListView(){
        stacksListView.setItems(prepareListView());
        stacksListView.setCellFactory(stackListView -> new StackListCell());
        stacksListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        stacksListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //add case: empty Stack
                currentFlashCardStack = stacksListView.getSelectionModel().getSelectedItem();
                currentFlashcard = currentFlashCardStack.getFlashcards().get(0);
                currentFlashcardIndex = 0;
                playVBox.setVisible(true);
                showQuestion(currentFlashcard);
            }
        });

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
        String content = flashcard.getHTMLContent();
        content = content.replace("contenteditable=\"true\"", "contenteditable=\"false\"");
        engine.loadContent(content, "text/html");
        answerButton.setVisible(true);
        nextButton.setVisible(false);
    }

    @FXML
    private void showAnswer(){
        String content = currentFlashcard.getHTMLContent();
        content = content.replace("contenteditable=\"true\"", "contenteditable=\"false\"");
        engine.loadContent(content, "text/html");
        answerButton.setVisible(false);
        nextButton.setVisible(true);
    }

    @FXML
    private void showNext(){
        if(++currentFlashcardIndex < currentFlashCardStack.getFlashcards().size()){
            currentFlashcard = currentFlashCardStack.getFlashcards().get(currentFlashcardIndex);
            showQuestion(currentFlashcard);
        }else {
            playVBox.setVisible(false);
        }
    }
}


