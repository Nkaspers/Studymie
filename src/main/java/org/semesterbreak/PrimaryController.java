package org.semesterbreak;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.paint.Color;
import javafx.util.Callback;
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

        stacksTreeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            private int lastSelected = -1;
            //Funktioniert noch nicht optimal werden wir eh umstrukturieren müssen
            //z.B. clicke auf eine karte, dann klappe den elternstapel ein und auf, die karte ist weiterhin
            //blau
            @Override
            public TreeCell<String> call(TreeView<String> stringTreeView) {
                stringTreeView.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
                    @Override
                    public void onChanged(Change<? extends Integer> change) {
                        if(change.getList().isEmpty()) return;

                        try {
                            TreeItem<String> item = stringTreeView.getTreeItem(lastSelected);
                            if(item != null && lastSelected != -1) {
                                item.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard.svg"));
                            }
                            int selectedIndex = change.getList().get(0);

                            //Nur mal beispielhaft:
                            activeFlashcardLabel.setText(String.valueOf(selectedIndex+1));
                            var selectedItem = stringTreeView.getTreeItem(selectedIndex);
                            if(selectedItem.isLeaf()) {
                                selectedItem.setGraphic(Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard_selected.svg"));
                                lastSelected = selectedIndex;
                            }
                        } catch (JDOMException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                var renderedCell = new TextFieldTreeCell<String>();
                renderedCell.prefWidthProperty().bind(stacksTreeView.prefWidthProperty().subtract(1));
                renderedCell.setStyle("-fx-padding: 8px 8px 8px 50px;");
                return renderedCell;
            }
        });

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

             for(int i = 0; i<5; i++) {
                 stack1.getChildren().add(new TreeItem<>("Karte hier könnte ihre Werbung stehen mit einem super langem Werbetext!!!" + i, Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard.svg")));
             }

             for(int i = 0; i<5; i++) {
                 TreeItem<String> treeItem = new TreeItem<>("Karte " + i, Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard.svg"));
                 stack2.getChildren().add(treeItem);
             }

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
