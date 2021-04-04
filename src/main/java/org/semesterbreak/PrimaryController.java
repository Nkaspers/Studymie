package org.semesterbreak;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
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
    public TreeItem<TreeViewElement> lastSelectedTreeViewItem;
    public FlashcardManager flashcardManager;
    public FlowPane flowPane;

    @FXML
    public void initialize() {
       flashcardManager = new FlashcardManager();

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

        stacksTreeView.setOnEditCommit(new EventHandler<TreeView.EditEvent<TreeViewElement>>() {
            @Override
            public void handle(TreeView.EditEvent<TreeViewElement> treeViewElementEditEvent) {
                System.out.println("Cell was edited");
            }
        });

        stacksTreeView.setCellFactory(new Callback<TreeView<TreeViewElement>, TreeCell<TreeViewElement>>() {
            @Override
            public TreeCell<TreeViewElement> call(TreeView<TreeViewElement> treeViewElementTreeView) {
                var custom = new CustomCell();
                custom.prefWidthProperty().bind(stacksTreeView.prefWidthProperty().subtract(5));

                custom.getAddFlashcardElementMenuItem().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        lastSelectedTreeViewItem = custom.getTreeItem();
                        addFlashcardAction();
                    }
                });

                custom.getDuplicateElementMenuItem().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        lastSelectedTreeViewItem = custom.getTreeItem();
                        duplicateElementAction();
                    }
                });

                custom.getDeleteElementMenuItem().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        lastSelectedTreeViewItem = custom.getTreeItem();
                        deleteElementAction();
                    }
                });

                return custom;
            }
        });

        stacksTreeView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<TreeViewElement>>() {
            @Override
            public void onChanged(Change<? extends TreeItem<TreeViewElement>> change) {
                if(change.getList().isEmpty()) return;
                TreeItem<TreeViewElement> item = change.getList().get(0);

                if(!item.getValue().isFlashcard()) {
                    activeFlashcardLabel.setText(String.valueOf(((FlashcardStack)item.getValue()).getFlashcards().size()));
                }else{
                    int index = item.getParent().getChildren().indexOf(item);
                    activeFlashcardLabel.setText(++index +"./"+item.getParent().getChildren().size());
                }

                lastSelectedTreeViewItem = item;
            }
        });

        stacksTreeView.setRoot(createTree());
        stacksTreeView.setShowRoot(false);
        stacksTreeView.setEditable(true);

        flowPane.setPrefWrapLength(1000);

        for(int i = 0; i<9; i++) {
            flowPane.getChildren().add(new FlashcardWebView());
        }
    }

    private void deleteElementAction() {
        if(lastSelectedTreeViewItem == null) return;

        if(lastSelectedTreeViewItem.getValue().isFlashcard()) {
            flashcardManager.removeFromStack((Flashcard)lastSelectedTreeViewItem.getValue());
            lastSelectedTreeViewItem.getParent().getChildren().remove(lastSelectedTreeViewItem);
        }else {
            flashcardManager.removeStack((FlashcardStack) lastSelectedTreeViewItem.getValue());
            stacksTreeView.getRoot().getChildren().remove(lastSelectedTreeViewItem);
        }

    }

    @FXML
    private void projectBtn() {

    }

    @FXML
    private void addFlashcardAction() {
        if(lastSelectedTreeViewItem == null) return;
        TreeItem<TreeViewElement> toAddToItem;
        toAddToItem = (lastSelectedTreeViewItem.getValue().isFlashcard())? lastSelectedTreeViewItem.getParent(): lastSelectedTreeViewItem;

        var flashcard = flashcardManager.addFlashcard((FlashcardStack) toAddToItem.getValue());

        TreeItem<TreeViewElement> treeItem = new TreeItem<>(flashcard);
        toAddToItem.getChildren().add(treeItem);
        stacksTreeView.getSelectionModel().select(treeItem);
    }

    @FXML
    private void addFlashcardStackAction() {
        var stack = flashcardManager.addFlashcardStack();
        stacksTreeView.getRoot().getChildren().add(new TreeItem<>(stack));
    }


    public TreeItem<TreeViewElement> createTree() {
        TreeItem<TreeViewElement> project1 = new TreeItem<>(null);
        for (FlashcardStack stack : flashcardManager.getStackList()) {
            TreeItem<TreeViewElement> treeItem = new TreeItem<>(stack);
            for (Flashcard f : stack.getFlashcards()) {
                TreeItem<TreeViewElement> flashcardTreeItem = new TreeItem<>(f);
                treeItem.getChildren().add(flashcardTreeItem);
            }
            project1.getChildren().add(treeItem);
        }
        return project1;
    }

    @FXML
    public void moveElementUpAction() {
        moveTreeElementAction(-1);
    }

    @FXML
    public void moveElementDownAction() {
        moveTreeElementAction(+1);
    }

    public void moveTreeElementAction(int indexShift){

        var parent = lastSelectedTreeViewItem.getParent();
        int index = parent.getChildren().indexOf(lastSelectedTreeViewItem);
        if ((index+indexShift< 0) || (index+indexShift > (parent.getChildren().size()-1))) return;
        var temp = parent.getChildren().get(index + indexShift);
        parent.getChildren().set(index, temp);
        parent.getChildren().set(index + indexShift, lastSelectedTreeViewItem);
        stacksTreeView.getSelectionModel().select(lastSelectedTreeViewItem);
        flashcardManager.moveTreeElement(lastSelectedTreeViewItem.getValue(), indexShift);
    }

    @FXML
    public void duplicateElementAction() {
        if(!lastSelectedTreeViewItem.getValue().isFlashcard()) return;
        Flashcard flashcardCopy = flashcardManager.duplicateFlashcard(lastSelectedTreeViewItem.getValue());
        int index = lastSelectedTreeViewItem.getParent().getChildren().indexOf(lastSelectedTreeViewItem);
        TreeItem<TreeViewElement> treeItem = new TreeItem<>(flashcardCopy);
        lastSelectedTreeViewItem.getParent().getChildren().add(index+1,treeItem);
        stacksTreeView.getSelectionModel().select(treeItem);
    }
}
