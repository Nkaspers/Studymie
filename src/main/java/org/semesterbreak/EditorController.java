package org.semesterbreak;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.jdom2.JDOMException;

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

    public FlashcardManager flashcardManager;

    public WebViewManager webViewManager;
    public ToggleButton makeUnderlinedButton;
    public ToggleButton makeItalicButton;
    public ToggleButton makeBoldButton;
    public ListView<Flashcard> flashcardListView;
    public FlashcardStack activeStack;

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

        initializeListView();

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
                        addFlashcardAction();
                    }
                });

                custom.getDuplicateElementMenuItem().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        duplicateElementAction();
                    }
                });

                custom.getDeleteElementMenuItem().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
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
                    activeStack = (FlashcardStack) item.getValue();
                    flashcardListView.getSelectionModel().select(null);
                    duplicateFlashcardButton.setDisable(true);
                } else {
                    int index = item.getParent().getChildren().indexOf(item);
                    activeFlashcardLabel.setText(++index + "./" + item.getParent().getChildren().size());
                    flashcardListView.getSelectionModel().select((Flashcard) item.getValue());
                    duplicateFlashcardButton.setDisable(false);
                }
            }
        });

        stacksTreeView.setRoot(createTree());
        stacksTreeView.setShowRoot(false);
        stacksTreeView.setEditable(true);
    }

    private void initializeListView() {
        flashcardListView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Flashcard>() {
            @Override
            public void onChanged(Change<? extends Flashcard> change) {
                if(change.getList().isEmpty()) return;
                stacksTreeView.getSelectionModel().select(findTreeItemFromFlashcardPane(change.getList().get(0)));
            }
        });

        flashcardListView.setCellFactory(new Callback<ListView<Flashcard>, ListCell<Flashcard>>() {
            @Override
            public ListCell<Flashcard> call(ListView<Flashcard> flashcardPaneListView) {
                CustomFlashcardCell cell = new CustomFlashcardCell();
                cell.setOnMouseClicked(new EventHandler<>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        changeSelectedFlashcardPaneAction(cell);
                    }
                });
                return cell;
            }
        });
    }
        private void changeSelectedFlashcardPaneAction(CustomFlashcardCell cell){

            stacksTreeView.getSelectionModel().select(cell.getIndex()+1);
        }

    private TreeItem<TreeViewElement> findTreeItemFromFlashcardPane(Flashcard flashcard) {
        for(TreeItem<TreeViewElement> stack : stacksTreeView.getRoot().getChildren()) {
            if(flashcard.getCurrentStack().equals(stack.getValue())) {
                for(TreeItem<TreeViewElement> flash : stack.getChildren()) {
                    if(flash.getValue().equals(flashcard)) {
                        return flash;
                    }
                }
            }
        }
        return null;
    }

    private void deleteElementAction() {
        var selectedTreeItem = stacksTreeView.getSelectionModel().getSelectedItems().get(0);

        if (selectedTreeItem == null) return;

        if (selectedTreeItem.getValue().isFlashcard()) {
            var selectedListViewItem = flashcardListView.getSelectionModel().getSelectedItems().get(0);

            flashcardManager.removeFromStack((Flashcard) selectedTreeItem.getValue());

            selectedTreeItem.getParent().getChildren().remove(selectedTreeItem);

            flashcardListView.getItems().remove(selectedListViewItem);
        } else {
            FlashcardStack stack = (FlashcardStack) selectedTreeItem.getValue();
            flashcardManager.removeStack(stack);
            stacksTreeView.getRoot().getChildren().remove(selectedTreeItem);
            if(stack.getFlashcards().isEmpty()) return;
            int first = 0;
            for(int i = 0; i < flashcardListView.getItems().size(); i++) {
                if(flashcardListView.getItems().get(i).equals(stack.getFlashcards().get(0))) first = i;
            }

            flashcardListView.getItems().remove(first, first+stack.getFlashcards().size());
        }

    }

    @FXML
    private void projectBtn() {

    }

    @FXML
    private void addFlashcardAction() {
        var selection = stacksTreeView.getSelectionModel().getSelectedItems().get(0);
        if (selection == null) return;
        TreeItem<TreeViewElement> toAddToItem;
        toAddToItem = (selection.getValue().isFlashcard()) ? selection.getParent() : selection;

        var stack = (FlashcardStack) toAddToItem.getValue();
        var flashcard = flashcardManager.addFlashcard(stack);

        TreeItem<TreeViewElement> treeItem = new TreeItem<>(flashcard);
        toAddToItem.getChildren().add(treeItem);

        int index = 0;
        for(int i = 0; i<flashcardManager.getStackList().size(); i++) {
            var tmpStack = flashcardManager.getStackList().get(i);
            if (stack.equals(tmpStack)) {
                index += tmpStack.getFlashcards().size() - 1;
                break;
            }
            index += tmpStack.getFlashcards().size();
        }

        flashcardListView.getItems().add(index, flashcard);

        stacksTreeView.getSelectionModel().select(treeItem);
        flashcardListView.getSelectionModel().select(flashcard);
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
                flashcardListView.getItems().add(f);
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
        var selection = stacksTreeView.getSelectionModel().getSelectedItems().get(0);
        var parent = selection.getParent();
        int index = parent.getChildren().indexOf(selection);
        if ((index + indexShift < 0) || (index + indexShift > (parent.getChildren().size() - 1))) return;
        var temp = parent.getChildren().get(index + indexShift);
        parent.getChildren().set(index, temp);
        parent.getChildren().set(index + indexShift, selection);

        stacksTreeView.getSelectionModel().select(selection);
        flashcardManager.moveTreeElement(selection.getValue(), indexShift);
    }

    @FXML
    public void duplicateElementAction() {
        var selection = stacksTreeView.getSelectionModel().getSelectedItems().get(0);
        var listViewSelection = flashcardListView.getSelectionModel().getSelectedItems().get(0);

        if (!selection.getValue().isFlashcard()) return;

        Flashcard flashcardCopy = flashcardManager.duplicateFlashcard(selection.getValue());

        int treeViewIndex = selection.getParent().getChildren().indexOf(selection);
        TreeItem<TreeViewElement> treeItem = new TreeItem<>(flashcardCopy);

        int listViewIndex = flashcardListView.getItems().indexOf(listViewSelection);

        selection.getParent().getChildren().add(treeViewIndex + 1, treeItem);
        stacksTreeView.getSelectionModel().select(treeItem);

        flashcardListView.getItems().add(listViewIndex + 1, flashcardCopy);
        flashcardListView.getSelectionModel().select(flashcardCopy);
    }

    @FXML
    public void makeUnderlinedAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItems().get(0);
        if(selection == null) return;
        /*var activeWebView = selection.getWebView();

        webViewManager.makeUnderlined(activeWebView);*/
    }

    @FXML
    public void makeItalicAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItems().get(0);
        if(selection == null) return;

        /*var activeWebView = selection.getWebView();

        webViewManager.makeItalic(activeWebView);*/
    }

    @FXML
    public void makeBoldAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItems().get(0);
        if(selection == null) return;

        /*var activeWebView = selection.getWebView();

        webViewManager.makeBold(activeWebView);*/
    }

    @FXML
    public void leftAlignAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItems().get(0);
        if(selection == null) return;

        /*var activeWebView = selection.getWebView();

        blockAlignButton.setSelected(false);
        rightAlignButton.setSelected(false);
        webViewManager.leftAlign(activeWebView);*/
    }

    @FXML
    public void blockAlignAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItems().get(0);
        if(selection == null) return;

        /*var activeWebView = selection.getWebView();

        leftAlignButton.setSelected(false);
        rightAlignButton.setSelected(false);
        webViewManager.blockAlign(activeWebView);*/
    }

    @FXML
    public void rightAlignAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItems().get(0);
        if(selection == null) return;

        /*var activeWebView = selection.getWebView();

        leftAlignButton.setSelected(false);
        blockAlignButton.setSelected(false);
        webViewManager.rightAlign(activeWebView);*/
    }

    @FXML
    public void addBulletListAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItems().get(0);
        if(selection == null) return;

        /*var activeWebView = selection.getWebView();

        webViewManager.addBulletList(activeWebView);*/
    }

    @FXML
    public void addNumberedListAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItems().get(0);
        if(selection == null) return;

        /*var activeWebView = selection.getWebView();

        webViewManager.addNumberedList(activeWebView);*/
    }
}
