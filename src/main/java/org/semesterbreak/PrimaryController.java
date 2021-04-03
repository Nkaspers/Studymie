package org.semesterbreak;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    public TreeView<TreeViewElement> stacksTreeView;
    public TreeItem<TreeViewElement> lastSelectedStack;
    public FlashcardManager flashcardManager = new FlashcardManager();

    @FXML
    public void initialize() {
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
        stacksTreeView.setEditable(true);
        stacksTreeView.setCellFactory(new Callback<TreeView<TreeViewElement>, TreeCell<TreeViewElement>>() {
            @Override
            public TreeCell<TreeViewElement> call(TreeView<TreeViewElement> treeViewElementTreeView) {
                return new CustomCell();
            }
        });
        stacksTreeView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<TreeViewElement>>() {
            @Override
            public void onChanged(Change<? extends TreeItem<TreeViewElement>> change) {
                if(change.getList().isEmpty()) return;
                TreeItem<TreeViewElement> item = change.getList().get(0);
                if(!item.getValue().isFlashcard()) {
                    lastSelectedStack = item;
                    activeFlashcardLabel.setText(String.valueOf(((FlashcardStack)item.getValue()).getFlashcards().size()));
                }else {
                    lastSelectedStack = item.getParent();
                }
            }
        });

        stacksTreeView.setRoot(createTree());
        stacksTreeView.setShowRoot(false);
        stacksTreeView.setEditable(true);
    }

    @FXML
    private void projectBtn() {

    }

    @FXML
    private void addFlashcardAction() {
        if(lastSelectedStack == null) return;
        var flashcard = flashcardManager.addFlashcard((FlashcardStack) lastSelectedStack.getValue());
        TreeItem<TreeViewElement> treeItem = new TreeItem<>(flashcard);
        lastSelectedStack.getChildren().add(treeItem);
        stacksTreeView.getSelectionModel().select(treeItem);
    }

    @FXML
    private void addFlashcardStackAction() {

    }

    @FXML
    public void addStackAction() {
        var stack = flashcardManager.addStack();
        stacksTreeView.getRoot().getChildren().add(new TreeItem<>(stack));
    }

    public TreeItem<TreeViewElement> createTree(){
        TreeItem<TreeViewElement> project1 = new TreeItem<TreeViewElement>(null);
        for(FlashcardStack stack : flashcardManager.getStackList()) {
            TreeItem<TreeViewElement> treeItem = new TreeItem<>(stack);
            for (Flashcard f : stack.getFlashcards()) {
                TreeItem<TreeViewElement> flashcardTreeItem = new TreeItem<>(f);
                treeItem.getChildren().add(flashcardTreeItem);
            }
            project1.getChildren().add(treeItem);
        }
        return project1;
    }


}
