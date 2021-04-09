package org.semesterbreak.scenes.editor;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;
import org.jdom2.JDOMException;
import org.semesterbreak.*;

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
    public Button addBulletListButton;
    public Button addNumberedListButton;
    public Label projectNameLabel;
    public Label activeFlashcardLabel;
    public ToggleButton leftAlignButton;
    public ToggleButton blockAlignButton;
    public ToggleButton rightAlignButton;
    public ToggleButton makeUnderlinedButton;
    public ToggleButton makeItalicButton;
    public ToggleButton makeBoldButton;
    public ChoiceBox<String> fontTypeCB;
    public ChoiceBox<Integer> fontSizeCB;
    public ColorPicker fontColorPicker;
    public TreeView<TreeViewElement> stacksTreeView;
    public ListView<FlashcardBridge> flashcardListView;

    private FlashcardStack activeStack;
    private FlashcardManager flashcardManager;
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

        initializeListView(this, flashcardManager, stacksTreeView, flashcardListView);
        initializeStacksTreeView();
    }

    private void initializeStacksTreeView() {
        stacksTreeView.setEditable(true);

        stacksTreeView.setOnEditCommit(treeViewElementEditEvent -> System.out.println("Cell was edited"));

        stacksTreeView.setCellFactory(treeViewElementTreeView -> {
            var custom = new CustomTreeViewCell();
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
                if (!item.getValue().equals(activeStack)) {
                    BasicEditorActions.loadNewStack(flashcardListView, (FlashcardStack) item.getValue());
                    activeStack = (FlashcardStack) item.getValue();
                }
            } else {
                int index = item.getParent().getChildren().indexOf(item);
                activeFlashcardLabel.setText(++index + "./" + item.getParent().getChildren().size());
                duplicateFlashcardButton.setDisable(false);
                if (!item.getParent().getValue().equals(activeStack)) {
                    var stack = (FlashcardStack) item.getParent().getValue();
                    BasicEditorActions.loadNewStack(flashcardListView, stack);
                    activeStack = stack;
                }
                BasicEditorActions.selectListViewElement(flashcardListView, (Flashcard) item.getValue());
            }
        });

        stacksTreeView.setRoot(BasicEditorActions.createTree(flashcardManager));
        stacksTreeView.setShowRoot(false);
        stacksTreeView.setEditable(true);
    }

    private void initializeListView(EditorController editorController, FlashcardManager flashcardManager, TreeView<TreeViewElement> stacksTreeView, ListView<FlashcardBridge> flashcardListView) {
        flashcardListView.setPlaceholder(new Label("Willkommen zum Karteikarten Editor."));
        flashcardListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<FlashcardBridge>) change -> {
            if (change.getList().isEmpty()) return;
            var selectedFlashcardBridge = change.getList().get(0);
            BasicEditorActions.selectTreeViewElement(flashcardManager, stacksTreeView, activeStack, selectedFlashcardBridge.getFlashcard());
        });

        flashcardListView.setCellFactory(flashcardPaneListView -> {
            CustomListViewCell cell = new CustomListViewCell();
            cell.setOnMouseClicked(mouseEvent -> {
                flashcardListView.getSelectionModel().select(cell.getItem());
                editorController.refreshQuestion(cell.getItem());
                editorController.updateFontProperties();
            });
            return cell;
        });
        if (!flashcardManager.getStackList().isEmpty()) {
            BasicEditorActions.loadNewStack(flashcardListView, flashcardManager.getStackList().get(0));
            activeStack = flashcardManager.getStackList().get(0);
        }
    }

    private void refreshQuestion(FlashcardBridge selectedFlashcardBridge) {
        var webView = selectedFlashcardBridge.getWebView();
        if(webView == null) return;
        selectedFlashcardBridge.getFlashcard().setQuestion(webViewManager.getQuestion(webView));
        stacksTreeView.refresh();
    }

    private void deleteElementAction() {
        BasicEditorActions.deleteTreeElement(flashcardManager, stacksTreeView, flashcardListView, activeFlashcardLabel);
    }

    private void updateFontProperties(){
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        WebView activeWebview = selection.getWebView();
        if(webViewManager.noSelection(activeWebview)) return;
        int size = webViewManager.getSelectionFontSize(activeWebview);
        Color color = webViewManager.getSelectionFontColor(activeWebview);
        fontSizeCB.setValue(size);
        fontColorPicker.setValue(color);
        try{
            String type = webViewManager.getSelectionFontType(activeWebview);
            //if(!fontTypeCB.getItems().contains(type)) return;
            fontTypeCB.setValue(type);
            System.out.println(type);
        }catch(JSException ignored){}
    }

    @FXML
    private void projectBtn() {

    }

    @FXML
    private void addFlashcardAction() {
        BasicEditorActions.addFlashcard(flashcardManager, stacksTreeView, flashcardListView);
    }

    @FXML
    private void addFlashcardStackAction() {
        BasicEditorActions.addFlashcardStack(flashcardManager, stacksTreeView);
    }


    @FXML
    public void moveElementUpAction() {
        BasicEditorActions.moveTreeElement(flashcardManager, stacksTreeView, flashcardListView, -1);
    }

    @FXML
    public void moveElementDownAction() {
        BasicEditorActions.moveTreeElement(flashcardManager, stacksTreeView, flashcardListView, +1);
    }

    @FXML
    public void duplicateElementAction() {
        BasicEditorActions.duplicateFlashcard(flashcardManager, stacksTreeView, flashcardListView);
    }

    @FXML
    public void makeUnderlinedAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
        if(activeWebView == null) return;

        webViewManager.makeUnderlined(activeWebView);
    }

    @FXML
    public void makeItalicAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
        if(activeWebView == null) return;

        webViewManager.makeItalic(activeWebView);
    }

    @FXML
    public void makeBoldAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
        if(activeWebView == null) return;

        webViewManager.makeBold(activeWebView);
    }

    @FXML
    private void colorPickerAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
        if(activeWebView == null) return;

        String color = fontColorPicker.getValue().toString().substring(2, 8);
        webViewManager.changeFontColor(activeWebView, color);
    }

    public void fontSizeAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
        if(activeWebView == null) return;

        webViewManager.changeFontSize(activeWebView, fontSizeCB.getValue());
    }

    public void fontTypeAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
        if(activeWebView == null) return;

        webViewManager.changeFontType(activeWebView, fontTypeCB.getValue());
    }

    @FXML
    public void leftAlignAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
        if(activeWebView == null) return;

        blockAlignButton.setSelected(false);
        rightAlignButton.setSelected(false);
        webViewManager.leftAlign(activeWebView);
    }

    @FXML
    public void blockAlignAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
        if(activeWebView == null) return;

        leftAlignButton.setSelected(false);
        rightAlignButton.setSelected(false);
        webViewManager.blockAlign(activeWebView);
    }

    @FXML
    public void rightAlignAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
        if(activeWebView == null) return;

        leftAlignButton.setSelected(false);
        blockAlignButton.setSelected(false);
        webViewManager.rightAlign(activeWebView);
    }

    @FXML
    public void addBulletListAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
        if(activeWebView == null) return;

        webViewManager.addBulletList(activeWebView);
    }

    @FXML
    public void addNumberedListAction() {
        var selection = flashcardListView.getSelectionModel().getSelectedItem();
        if (selection == null) return;
        var activeWebView = selection.getWebView();
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
