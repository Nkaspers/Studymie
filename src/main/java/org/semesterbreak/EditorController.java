package org.semesterbreak;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.paint.Color;
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

    public ToggleButton makeUnderlinedButton;
    public ToggleButton makeItalicButton;
    public ToggleButton makeBoldButton;
    public ListView<FlashcardBridge> flashcardListView;
    public FlashcardStack activeStack;
    private WebViewManager webViewManager;

    @FXML
    public void initialize() {
        flashcardManager = new FlashcardManager();
        webViewManager = new WebViewManager();

        initializeButtonGraphics();

        fontColorPicker.setValue(Color.BLACK);

        fontSizeCB.getItems().addAll(1, 2, 3, 4, 5, 6, 7);
        fontSizeCB.setValue(3);
        fontSizeCB.setOnAction(actionEvent -> fontSizeAction());

        fontTypeCB.getItems().addAll(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontTypeCB.setValue("Arial");
        fontTypeCB.setOnAction(actionEvent -> fontTypeAction());

        initializeListView();
        initializeStacksTreeView();
    }

    private void initializeStacksTreeView() {
        stacksTreeView.setEditable(true);

        stacksTreeView.setOnEditCommit(treeViewElementEditEvent -> System.out.println("Cell was edited"));

        stacksTreeView.setCellFactory(treeViewElementTreeView -> {
            var custom = new CustomCell();
            custom.prefWidthProperty().bind(stacksTreeView.prefWidthProperty().subtract(5));
            custom.getAddFlashcardElementMenuItem().setOnAction(actionEvent -> addFlashcardAction());
            custom.getDuplicateElementMenuItem().setOnAction(actionEvent -> duplicateElementAction());
            custom.getDeleteElementMenuItem().setOnAction(actionEvent -> deleteElementAction());

            return custom;
        });

        stacksTreeView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<TreeItem<TreeViewElement>>) change -> {
            if (change.getList().isEmpty()) return;
            TreeItem<TreeViewElement> item = change.getList().get(0);

            if (!item.getValue().isFlashcard()) {
                activeFlashcardLabel.setText(String.valueOf(((FlashcardStack) item.getValue()).getFlashcards().size()));
                flashcardListView.getSelectionModel().select(null);
                duplicateFlashcardButton.setDisable(true);
                if (!item.getValue().equals(activeStack)) loadNewStack((FlashcardStack) item.getValue());
            } else {
                int index = item.getParent().getChildren().indexOf(item);
                activeFlashcardLabel.setText(++index + "./" + item.getParent().getChildren().size());
                duplicateFlashcardButton.setDisable(false);
                if (!item.getParent().getValue().equals(activeStack))
                    loadNewStack((FlashcardStack) item.getParent().getValue());
                selectListViewElement((Flashcard) item.getValue());
            }
        });

        stacksTreeView.setRoot(createTree());
        stacksTreeView.setShowRoot(false);
        stacksTreeView.setEditable(true);
    }

    private void initializeListView() {
        flashcardListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<FlashcardBridge>) change -> {
            if (change.getList().isEmpty()) return;
            var selectedFlashcardBridge = change.getList().get(0);
            selectTreeViewElement(selectedFlashcardBridge.getFlashcard());
            refreshQuestion(selectedFlashcardBridge);
        });

        flashcardListView.setCellFactory(flashcardPaneListView -> {
            CustomFlashcardCell cell = new CustomFlashcardCell();
            cell.setOnMouseClicked(mouseEvent -> {
                flashcardListView.getSelectionModel().select(cell.getItem());
                refreshQuestion(cell.getItem());
            });
            return cell;
        });
        if (!flashcardManager.getStackList().isEmpty()) loadNewStack(flashcardManager.getStackList().get(0));
    }

    private void refreshQuestion(FlashcardBridge selectedFlashcardBridge) {
        selectedFlashcardBridge.getFlashcard().setQuestion(webViewManager.getQuestion(selectedFlashcardBridge.getWebView()));
    }

    private void selectListViewElement(Flashcard flashcard) {
        FlashcardBridge toBeSelected = null;
        for (FlashcardBridge bridge : flashcardListView.getItems()) {
            if (bridge.getFlashcard().equals(flashcard)) toBeSelected = bridge;
        }
        flashcardListView.getSelectionModel().select(toBeSelected);
    }

    private void loadNewStack(FlashcardStack stack) {
        flashcardListView.getItems().remove(0, flashcardListView.getItems().size());
        for (Flashcard f : stack.getFlashcards()) {
            flashcardListView.getItems().add(new FlashcardBridge(f));
        }
        activeStack = stack;
    }

    private void selectTreeViewElement(Flashcard flashcard) {
        int stackIndex = flashcardManager.getStackList().indexOf(activeStack);
        int flashcardIndex = activeStack.getFlashcards().indexOf(flashcard);
        TreeItem<TreeViewElement> item = stacksTreeView.getRoot().getChildren().get(stackIndex).getChildren().get(flashcardIndex);
        stacksTreeView.getSelectionModel().select(item);
    }

    private void deleteElementAction() {
        var selectedTreeItem = stacksTreeView.getSelectionModel().getSelectedItem();

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
            if (stack.getFlashcards().isEmpty()) {
                stacksTreeView.getSelectionModel().clearSelection();
                return;
            }
            flashcardListView.getItems().removeAll(flashcardListView.getItems());
            if (!stacksTreeView.getRoot().getChildren().isEmpty()) {
                var newSelectedStack = (FlashcardStack) stacksTreeView.getSelectionModel().getSelectedItem().getValue();
                loadNewStack(newSelectedStack);
            } else {
                stacksTreeView.getSelectionModel().clearSelection();
            }
        }

    }

    @FXML
    private void projectBtn() {

    }

    @FXML
    private void addFlashcardAction() {
        var selection = stacksTreeView.getSelectionModel().getSelectedItem();
        if (selection == null) return;

        TreeItem<TreeViewElement> toAddToItem;
        toAddToItem = (selection.getValue().isFlashcard()) ? selection.getParent() : selection;

        var stack = (FlashcardStack) toAddToItem.getValue();
        var flashcard = flashcardManager.addFlashcard(stack);

        TreeItem<TreeViewElement> treeItem = new TreeItem<>(flashcard);
        toAddToItem.getChildren().add(treeItem);

        int index = stack.getFlashcards().size() - 1;

        FlashcardBridge flashcardBridge = new FlashcardBridge(flashcard);
        flashcardListView.getItems().add(index, flashcardBridge);
        flashcardListView.getSelectionModel().select(flashcardBridge);
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

    public void moveTreeElementAction(int indexShift) {
        var selectedTreeItem = stacksTreeView.getSelectionModel().getSelectedItem();
        var parent = selectedTreeItem.getParent();
        int index = parent.getChildren().indexOf(selectedTreeItem);
        if ((index + indexShift < 0) || (index + indexShift > parent.getChildren().size() - 1)) return;

        parent.getChildren().remove(index);
        parent.getChildren().add(index + indexShift, selectedTreeItem);

        if (selectedTreeItem.getValue().isFlashcard()) {
            flashcardManager.moveFlashcard((Flashcard) selectedTreeItem.getValue(), indexShift);
            var listViewItem = flashcardListView.getItems().remove(index);
            flashcardListView.getItems().add(index + indexShift, listViewItem);
            selectListViewElement((Flashcard) selectedTreeItem.getValue());
        } else {
            flashcardManager.moveFlashcardStack((FlashcardStack) selectedTreeItem.getValue(), indexShift);
        }
        var card = selectedTreeItem.getValue();
        stacksTreeView.getSelectionModel().select(selectedTreeItem);
    }


    @FXML
    public void duplicateElementAction() {
        var selection = stacksTreeView.getSelectionModel().getSelectedItem();
        var listViewSelection = flashcardListView.getSelectionModel().getSelectedItem();

        if (!selection.getValue().isFlashcard()) return;

        Flashcard flashcardCopy = flashcardManager.duplicateFlashcard((Flashcard) selection.getValue());

        int treeViewIndex = selection.getParent().getChildren().indexOf(selection);
        TreeItem<TreeViewElement> treeItem = new TreeItem<>(flashcardCopy);
        FlashcardBridge bridge = new FlashcardBridge(flashcardCopy);
        int listViewIndex = flashcardListView.getItems().indexOf(listViewSelection);

        selection.getParent().getChildren().add(treeViewIndex + 1, treeItem);
        stacksTreeView.getSelectionModel().select(treeItem);

        flashcardListView.getItems().add(listViewIndex + 1, bridge);
        flashcardListView.getSelectionModel().select(bridge);
    }

    @FXML
    public void makeUnderlinedAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();

        webViewManager.makeUnderlined(activeWebView);
    }

    @FXML
    public void makeItalicAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();

        webViewManager.makeItalic(activeWebView);
    }

    @FXML
    public void makeBoldAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();

        webViewManager.makeBold(activeWebView);
    }

    @FXML
    private void colorPickerAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
        String color = fontColorPicker.getValue().toString().substring(2, 8);
        webViewManager.changeFontColor(activeWebView, color);
    }

    public void fontSizeAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
        webViewManager.changeFontSize(activeWebView, fontSizeCB.getValue());
    }

    public void fontTypeAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
        webViewManager.changeFontType(activeWebView, fontTypeCB.getValue());
    }

    @FXML
    public void leftAlignAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();

        blockAlignButton.setSelected(false);
        rightAlignButton.setSelected(false);
        webViewManager.leftAlign(activeWebView);
    }

    @FXML
    public void blockAlignAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();

        leftAlignButton.setSelected(false);
        rightAlignButton.setSelected(false);
        webViewManager.blockAlign(activeWebView);
    }

    @FXML
    public void rightAlignAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();

        leftAlignButton.setSelected(false);
        blockAlignButton.setSelected(false);
        webViewManager.rightAlign(activeWebView);
    }

    @FXML
    public void addBulletListAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();

        webViewManager.addBulletList(activeWebView);
    }

    @FXML
    public void addNumberedListAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();

        webViewManager.addNumberedList(activeWebView);
    }

    private void initializeButtonGraphics() {
        try {
            undoButton.setGraphic(Utilities.getIconGroup("undo.svg", true));
            redoButton.setGraphic(Utilities.getIconGroup("redo.svg", true));
            exportButton.setGraphic(Utilities.getIconGroup("export.svg", true));
            addStackButton.setGraphic(Utilities.getIconGroup("add_stack.svg", true));
            playButton.setGraphic(Utilities.getIconGroup("play_button.svg", true));
            addFlashcardButton.setGraphic(Utilities.getIconGroup("add_flashcard.svg", true));

            leftAlignButton.setGraphic(Utilities.getIconGroup("text_left_align.svg", true));
            blockAlignButton.setGraphic(Utilities.getIconGroup("text_block_align.svg", true));
            rightAlignButton.setGraphic(Utilities.getIconGroup("text_right_align.svg", true));
            addBulletListButton.setGraphic(Utilities.getIconGroup("add_bullet_list.svg", true));
            addNumberedListButton.setGraphic(Utilities.getIconGroup("add_numbered_list.svg", true));

            moveFlashcardDownButton.setGraphic(Utilities.getIconGroup("move_down.svg", true));
            moveFlashcardUpButton.setGraphic(Utilities.getIconGroup("move_up.svg", true));
            duplicateFlashcardButton.setGraphic(Utilities.getIconGroup("duplicate.svg", true));
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }
}
