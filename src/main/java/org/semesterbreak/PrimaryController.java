package org.semesterbreak;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PrimaryController {


    public Button addStackButton;
    public Button playButton;
    public Button addFlashcardButton;
    public Button undoButton;
    public Button redoButton;
    public Button exportButton;
    public Button projectButton;


    @FXML
    public void initialize(){
        //projectButton.setGraphic(EditorUI.projectButtonIcon);
        undoButton.setGraphic(EditorUI.undoButtonIcon);
        redoButton.setGraphic(EditorUI.redoButtonIcon);
        exportButton.setGraphic(EditorUI.exportButtonIcon);
        addStackButton.setGraphic(EditorUI.addStackButtonIcon);
        playButton.setGraphic(EditorUI.PlayButtonIcon);
        addFlashcardButton.setGraphic(EditorUI.addFlashCardButtonIcon);

    }

    @FXML
    private void projectBtn() {

    }


}
