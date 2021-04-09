package org.semesterbreak.scenes.editor;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.semesterbreak.Flashcard;
import org.semesterbreak.FlashcardManager;
import org.semesterbreak.FlashcardStack;
import org.semesterbreak.TreeViewElement;

public class BasicEditorActions {

    public static void addFlashcard(FlashcardManager flashcardManager, TreeView<TreeViewElement> stacksTreeView, ListView<FlashcardBridge> flashcardListView) {
        var selection = stacksTreeView.getSelectionModel().getSelectedItem();
        if (selection == null) return;

        TreeItem<TreeViewElement> toAddToItem = (selection.getValue().isFlashcard()) ? selection.getParent() : selection;

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

    public static void addFlashcardStack(FlashcardManager flashcardManager, TreeView<TreeViewElement> stacksTreeView) {
        var stack = flashcardManager.addFlashcardStack();
        stacksTreeView.getRoot().getChildren().add(new TreeItem<>(stack));
    }

    static void moveTreeElement(FlashcardManager flashcardManager, TreeView<TreeViewElement> stacksTreeView, ListView<FlashcardBridge> flashcardListView, int indexShift) {
        var selectedTreeItem = stacksTreeView.getSelectionModel().getSelectedItem();
        if(selectedTreeItem == null) return;

        var parent = selectedTreeItem.getParent();
        int index = parent.getChildren().indexOf(selectedTreeItem);
        if ((index + indexShift < 0) || (index + indexShift > parent.getChildren().size() - 1)) return;

        parent.getChildren().remove(index);
        parent.getChildren().add(index + indexShift, selectedTreeItem);

        if (selectedTreeItem.getValue().isFlashcard()) {
            flashcardManager.moveFlashcard((Flashcard) selectedTreeItem.getValue(), indexShift);
            var listViewItem = flashcardListView.getItems().remove(index);
            flashcardListView.getItems().add(index + indexShift, listViewItem);
            selectListViewElement(flashcardListView,(Flashcard) selectedTreeItem.getValue());
        } else {
            flashcardManager.moveFlashcardStack((FlashcardStack) selectedTreeItem.getValue(), indexShift);
        }
        stacksTreeView.getSelectionModel().select(selectedTreeItem);
    }

    static void selectListViewElement(ListView<FlashcardBridge> flashcardListView, Flashcard flashcard) {
        FlashcardBridge toBeSelected = null;
        for (FlashcardBridge bridge : flashcardListView.getItems()) {
            if (bridge.getFlashcard().equals(flashcard)) toBeSelected = bridge;
        }
        flashcardListView.getSelectionModel().select(toBeSelected);
    }

    static void selectTreeViewElement(FlashcardManager flashcardManager, TreeView<TreeViewElement> stacksTreeView, FlashcardStack activeStack, Flashcard flashcard) {
        int stackIndex = flashcardManager.getStackList().indexOf(activeStack);
        int flashcardIndex = activeStack.getFlashcards().indexOf(flashcard);
        TreeItem<TreeViewElement> item = stacksTreeView.getRoot().getChildren().get(stackIndex).getChildren().get(flashcardIndex);
        stacksTreeView.getSelectionModel().select(item);
    }

    static void deleteTreeElement(FlashcardManager flashcardManager, TreeView<TreeViewElement> stacksTreeView, ListView<FlashcardBridge> flashcardListView, Label activeFlashcardLabel) {
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
                loadNewStack(flashcardListView, newSelectedStack);
            } else {
                stacksTreeView.getSelectionModel().clearSelection();
                activeFlashcardLabel.setText("-");
            }
        }
    }

    static void duplicateFlashcard(FlashcardManager flashcardManager, TreeView<TreeViewElement> stacksTreeView, ListView<FlashcardBridge> flashcardListView) {
        var selection = stacksTreeView.getSelectionModel().getSelectedItem();
        if(selection == null) return;

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

    static void loadNewStack(ListView<FlashcardBridge> flashcardListView, FlashcardStack stack) {
        flashcardListView.getItems().remove(0, flashcardListView.getItems().size());
        for (Flashcard f : stack.getFlashcards()) {
            flashcardListView.getItems().add(new FlashcardBridge(f));
        }
    }

    public static TreeItem<TreeViewElement> createTree(FlashcardManager flashcardManager) {
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
}
