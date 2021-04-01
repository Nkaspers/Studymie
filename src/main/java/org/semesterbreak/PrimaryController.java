package org.semesterbreak;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.jdom2.JDOMException;

import java.awt.*;
import java.io.IOException;

public class PrimaryController {


    public Button addStackButton;
    public Button playButton;
    public Button addFlashcardButton;
    public Button undoButton;
    public Button redoButton;
    public Button exportButton;
    public Button projectButton;
    public Button moveFlashcardUpButton;
    public Button moveFlashcardDownButton;
    public Button duplicateFlashcardButton;
    public ChoiceBox<String> fontTypeCB;
    public ColorPicker fontColorPicker;
    public ChoiceBox<Integer> fontSizeCB;
    public Label projectNameLabel;
    public Label activeFlashcardLabel;
    public ToggleButton leftAlignButton;
    public ToggleButton blockAlignButton;
    public ToggleButton rightAlignButton;
    public Button addBulletListButton;
    public Button addNumberedListButton;
    public TreeView<String> stacksTreeView;




    @FXML
    public void initialize(){
        try {
            undoButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/undo.svg"));
            redoButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/redo.svg"));
            exportButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/export.svg"));
            addStackButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/add_stack.svg"));
            playButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/play_button.svg"));
            addFlashcardButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/add_flashcard.svg"));

            leftAlignButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/text_left_align.svg"));
            blockAlignButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/text_block_align.svg"));
            rightAlignButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/text_right_align.svg"));
            addBulletListButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/add_bullet_list.svg"));
            addNumberedListButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/add_numbered_list.svg"));

            moveFlashcardDownButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/move_down.svg"));
            moveFlashcardUpButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/move_up.svg"));
            duplicateFlashcardButton.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/duplicate.svg"));
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }

        fontColorPicker.setValue(Color.BLACK);

        fontSizeCB.getItems().addAll(1, 2, 3, 4, 5, 6, 7);
        fontSizeCB.setValue(4);

        fontTypeCB.getItems().addAll(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontTypeCB.setValue("Arial");

        stacksTreeView.setRoot(getExampleTree());
        stacksTreeView.setShowRoot(false);
    }

    @FXML
    private void projectBtn() {

    }

    public TreeItem<String> getExampleTree(){
         TreeItem<String> project1 = new TreeItem<>("Projekt1");
         try{

         TreeItem<String> stack1 = new TreeItem<>("Stapel1",Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/stack_icon.svg"));
         TreeItem<String> stack2 = new TreeItem<>("Stapel2",Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/stack_icon.svg"));

            stack1.getChildren().addAll(
                    new TreeItem<String>("Karte1", Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard.svg")),
                    new TreeItem<String>("Karte2", Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard.svg")),
                    new TreeItem<String>("Karte3", Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard.svg")),
                    new TreeItem<String>("Karte4", Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard.svg"))
            );
            stack2.getChildren().addAll(
                    new TreeItem<String>("Karte1", Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard.svg")),
                    new TreeItem<String>("Karte2", Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard.svg")),
                    new TreeItem<String>("Karte3", Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard.svg")),
                    new TreeItem<String>("Karte4", Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard.svg"))
            );

             stack1.setExpanded(true);
             stack2.setExpanded(true);
             project1.getChildren().addAll(stack1,stack2);
             return(project1);

        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
