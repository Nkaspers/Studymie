package org.semesterbreak;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.jdom2.JDOMException;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class EditorController {


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
    public WebView activeWebView;
    public WebViewManager webViewManager;
    public ToggleButton makeUnderlinedButton;
    public ToggleButton makeItalicButton;
    public ToggleButton makeBoldButton;
    public ListView<FlashcardPane> flashcardView;

    @FXML
    public void initialize() {
        flashcardManager = new FlashcardManager();
        webViewManager = new WebViewManager();

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

        flashcardView.setCellFactory(new Callback<ListView<FlashcardPane>, ListCell<FlashcardPane>>() {
            @Override
            public ListCell<FlashcardPane> call(ListView<FlashcardPane> flashcardView) {
                return new CustomFlashcardCell();
            }
        });

        flashcardView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<FlashcardPane>() {
            @Override
            public void onChanged(Change<? extends FlashcardPane> change) {
                if(change.getList().isEmpty()) return;
                Flashcard flashcard = change.getList().get(0).getFlashcard();
                for(TreeItem<TreeViewElement> stack : stacksTreeView.getRoot().getChildren()) {
                    if(flashcard.getCurrentStack().equals(stack.getValue())) {
                        for(TreeItem<TreeViewElement> flash : stack.getChildren()) {
                            if(flash.getValue().equals(flashcard)) {
                                stacksTreeView.getSelectionModel().select(flash);
                                lastSelectedTreeViewItem = flash;
                                break;
                            }
                        }
                    }
                }
            }
        });

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
                if (change.getList().isEmpty()) return;
                TreeItem<TreeViewElement> item = change.getList().get(0);

                if (!item.getValue().isFlashcard()) {
                    activeFlashcardLabel.setText(String.valueOf(((FlashcardStack) item.getValue()).getFlashcards().size()));
                } else {
                    int index = item.getParent().getChildren().indexOf(item);
                    activeFlashcardLabel.setText(++index + "./" + item.getParent().getChildren().size());
                    for(FlashcardPane p : flashcardView.getItems()) {
                        if(p.getFlashcard().equals(item.getValue())) {
                            flashcardView.getSelectionModel().select(p);
                        }
                    }
                }

                lastSelectedTreeViewItem = item;
            }
        });

        stacksTreeView.setRoot(createTree());
        stacksTreeView.setShowRoot(false);
        stacksTreeView.setEditable(true);
    }

    private void deleteElementAction() {
        if (lastSelectedTreeViewItem == null) return;

        if (lastSelectedTreeViewItem.getValue().isFlashcard()) {
            flashcardManager.removeFromStack((Flashcard) lastSelectedTreeViewItem.getValue());
            lastSelectedTreeViewItem.getParent().getChildren().remove(lastSelectedTreeViewItem);
        } else {
            flashcardManager.removeStack((FlashcardStack) lastSelectedTreeViewItem.getValue());
            stacksTreeView.getRoot().getChildren().remove(lastSelectedTreeViewItem);
        }

    }

    @FXML
    private void projectBtn() {

    }

    @FXML
    private void addFlashcardAction() {
        if (lastSelectedTreeViewItem == null) return;
        TreeItem<TreeViewElement> toAddToItem;
        toAddToItem = (lastSelectedTreeViewItem.getValue().isFlashcard()) ? lastSelectedTreeViewItem.getParent() : lastSelectedTreeViewItem;

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
                FlashcardPane pane = new FlashcardPane(f);
                flashcardView.getItems().add(pane);
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

    public void moveTreeElementAction(int indexShift) {

        var parent = lastSelectedTreeViewItem.getParent();
        int index = parent.getChildren().indexOf(lastSelectedTreeViewItem);
        if ((index + indexShift < 0) || (index + indexShift > (parent.getChildren().size() - 1))) return;
        var temp = parent.getChildren().get(index + indexShift);
        parent.getChildren().set(index, temp);
        parent.getChildren().set(index + indexShift, lastSelectedTreeViewItem);
        stacksTreeView.getSelectionModel().select(lastSelectedTreeViewItem);
        flashcardManager.moveTreeElement(lastSelectedTreeViewItem.getValue(), indexShift);
    }

    @FXML
    public void duplicateElementAction() {
        if (!lastSelectedTreeViewItem.getValue().isFlashcard()) return;
        Flashcard flashcardCopy = flashcardManager.duplicateFlashcard(lastSelectedTreeViewItem.getValue());
        int index = lastSelectedTreeViewItem.getParent().getChildren().indexOf(lastSelectedTreeViewItem);
        TreeItem<TreeViewElement> treeItem = new TreeItem<>(flashcardCopy);
        lastSelectedTreeViewItem.getParent().getChildren().add(index + 1, treeItem);
        stacksTreeView.getSelectionModel().select(treeItem);
    }

    @FXML
    public void makeUnderlinedAction() {
        webViewManager.makeUnderlined(activeWebView);
    }

    @FXML
    public void makeItalicAction() {
        webViewManager.makeItalic(activeWebView);
    }

    @FXML
    public void makeBoldAction() {
        webViewManager.makeBold(activeWebView);
    }

    @FXML
    public void leftAlignAction() {
        blockAlignButton.setSelected(false);
        rightAlignButton.setSelected(false);
        webViewManager.leftAlign(activeWebView);
    }

    @FXML
    public void blockAlignAction() {
        leftAlignButton.setSelected(false);
        rightAlignButton.setSelected(false);
        webViewManager.blockAlign(activeWebView);
    }

    @FXML
    public void rightAlignAction() {
        leftAlignButton.setSelected(false);
        blockAlignButton.setSelected(false);
        webViewManager.rightAlign(activeWebView);
    }

    @FXML
    public void addBulletListAction() {
        webViewManager.addBulletList(activeWebView);
    }

    @FXML
    public void addNumberedListAction() {
        webViewManager.addNumberedList(activeWebView);
    }
}