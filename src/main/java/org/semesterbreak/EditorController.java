package org.semesterbreak;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
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

    public WebView activeWebView;
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
        fontSizeCB.setValue(4);

        fontTypeCB.getItems().addAll(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontTypeCB.setValue("Arial");

        initializeListView();
        initializeStacksTreeView();
    }

    private void initializeStacksTreeView() {
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
                        //duplicateElementAction();
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
                    flashcardListView.getSelectionModel().select(null);
                    duplicateFlashcardButton.setDisable(true);
                    if(!item.getValue().equals(activeStack)) loadNewStack((FlashcardStack) item.getValue());
                } else {
                    int index = item.getParent().getChildren().indexOf(item);
                    activeFlashcardLabel.setText(++index + "./" + item.getParent().getChildren().size());
                    duplicateFlashcardButton.setDisable(false);
                    if(!item.getParent().getValue().equals(activeStack)) loadNewStack((FlashcardStack) item.getParent().getValue());
                    selectListViewElement((Flashcard) item.getValue());
                }
            }
        });

        stacksTreeView.setRoot(createTree());
        stacksTreeView.setShowRoot(false);
        stacksTreeView.setEditable(true);
        stacksTreeView.getSelectionModel().select(stacksTreeView.getRoot().getChildren().get(0));
    }

    private void initializeListView() {
        flashcardListView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<FlashcardBridge>() {
            @Override
            public void onChanged(Change<? extends FlashcardBridge> change) {
                if(change.getList().isEmpty()) return;
                selectTreeViewElement(change.getList().get(0).getFlashcard());
                activeWebView = change.getList().get(0).getWebView();
            }
        });

        flashcardListView.setCellFactory(new Callback<ListView<FlashcardBridge>, ListCell<FlashcardBridge>>() {

            @Override
            public ListCell<FlashcardBridge> call(ListView<FlashcardBridge> flashcardPaneListView) {
                CustomFlashcardCell cell = new CustomFlashcardCell();
                cell.setOnMouseClicked(new EventHandler<>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        flashcardListView.getSelectionModel().select(cell.getItem());
                    }
                });
                return cell;
            }
        });
        loadNewStack(flashcardManager.getStackList().get(0));
    }

    private void selectListViewElement(Flashcard flashcard) {
        FlashcardBridge toBeSelected = null;
        for(FlashcardBridge bridge: flashcardListView.getItems()){
            if(bridge.getFlashcard().equals(flashcard)) toBeSelected = bridge;
        }
        flashcardListView.getSelectionModel().select(toBeSelected);
        flashcardListView.scrollTo(toBeSelected);
    }

    private void loadNewStack(FlashcardStack stack){
        flashcardListView.getItems().remove(0,flashcardListView.getItems().size());
        for(Flashcard f: stack.getFlashcards()){
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
                if(flashcardListView.getItems().get(i).getFlashcard().equals(stack.getFlashcards().get(0))) first = i;
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
        FlashcardBridge flashcardBridge = new FlashcardBridge(flashcard);
        flashcardListView.getItems().add(index, flashcardBridge);

        stacksTreeView.getSelectionModel().select(treeItem);
        flashcardListView.getSelectionModel().select(flashcardBridge);
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
                FlashcardBridge bridge = new FlashcardBridge(f);
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
        /*var selection = stacksTreeView.getSelectionModel().getSelectedItems().get(0);
        var listViewSelection = flashcardListView.getSelectionModel().getSelectedItems().get(0);

        if (!selection.getValue().isFlashcard()) return;

        Flashcard flashcardCopy = flashcardManager.duplicateFlashcard(selection.getValue());

        int treeViewIndex = selection.getParent().getChildren().indexOf(selection);
        TreeItem<TreeViewElement> treeItem = new TreeItem<>(flashcardCopy);

        int listViewIndex = flashcardListView.getItems().indexOf(listViewSelection);

        selection.getParent().getChildren().add(treeViewIndex + 1, treeItem);
        stacksTreeView.getSelectionModel().select(treeItem);

        flashcardListView.getItems().add(listViewIndex + 1, flashcardCopy);
        flashcardListView.getSelectionModel().select(flashcardCopy);*/
    }

    @FXML
    public void makeUnderlinedAction() {
        if(activeWebView == null) return;
        webViewManager.makeUnderlined(activeWebView);
    }

    @FXML
    public void makeItalicAction() {
        if(activeWebView == null) return;

        webViewManager.makeItalic(activeWebView);
    }

    @FXML
    public void makeBoldAction() {
        if(activeWebView == null) return;

        webViewManager.makeBold(activeWebView);
    }

    @FXML
    public void leftAlignAction() {
        if(activeWebView == null) return;

        blockAlignButton.setSelected(false);
        rightAlignButton.setSelected(false);
        webViewManager.leftAlign(activeWebView);
    }

    @FXML
    public void blockAlignAction() {
        if(activeWebView == null) return;

        leftAlignButton.setSelected(false);
        rightAlignButton.setSelected(false);
        webViewManager.blockAlign(activeWebView);
    }

    @FXML
    public void rightAlignAction() {
        if(activeWebView == null) return;

        leftAlignButton.setSelected(false);
        blockAlignButton.setSelected(false);
        webViewManager.rightAlign(activeWebView);
    }

    @FXML
    public void addBulletListAction() {
        if(activeWebView == null) return;

        webViewManager.addBulletList(activeWebView);
    }

    @FXML
    public void addNumberedListAction() {
        if(activeWebView == null) return;

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
