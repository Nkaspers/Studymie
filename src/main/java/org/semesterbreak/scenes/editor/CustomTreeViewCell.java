package org.semesterbreak.scenes.editor;

import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.StringConverter;
import org.jdom2.JDOMException;
import org.semesterbreak.Flashcard;
import org.semesterbreak.FlashcardStack;
import org.semesterbreak.TreeViewElement;
import org.semesterbreak.Utilities;

import java.io.IOException;

public class CustomTreeViewCell extends TextFieldTreeCell<TreeViewElement> {
    private Group iconFlash;
    private Group iconFlashSelected;
    private Group iconFlashStack;
    private ContextMenu rightClickMenu;

    private MenuItem deleteElementMenuItem;
    private MenuItem duplicateElementMenuItem;
    private MenuItem addFlashcardElementMenuItem;

    public CustomTreeViewCell() {
        try {
            iconFlashSelected = Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard_selected.svg");
            iconFlash = Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard.svg");
            iconFlashStack = Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/stack_icon.svg");
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }

        setConverter(new StringConverter<TreeViewElement>() {
            @Override
            public String toString(TreeViewElement treeViewElement) {
                if(treeViewElement.isFlashcard()) {
                    return ((Flashcard) treeViewElement).getQuestion();
                }else {
                    return ((FlashcardStack) treeViewElement).getTitle();
                }
            }

            @Override
            public TreeViewElement fromString(String s) {

                if(getItem().isFlashcard()) {
                    ((Flashcard) getItem()).setQuestion(s);
                }else {
                    ((FlashcardStack) getItem()).setTitle(s);
                }
                return getItem();
            }
        });

        rightClickMenu = new ContextMenu();

        deleteElementMenuItem = new MenuItem("L??schen");
        duplicateElementMenuItem = new MenuItem("Duplizieren");
        addFlashcardElementMenuItem = new MenuItem("Karteikarte hinzuf??gen");

        setContextMenu(rightClickMenu);
    }

    @Override
    public void updateItem(TreeViewElement item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
            setContextMenu(null);
            return;
        }

        if (item.isFlashcard()) {
            Flashcard flashcard = (Flashcard) item;
            setText(flashcard.getQuestion());
            if (isSelected()) {
                setGraphic(iconFlashSelected);
            } else {
                setGraphic(iconFlash);
            }

            setTooltip(new Tooltip(flashcard.getQuestion()));
            rightClickMenu.getItems().setAll(duplicateElementMenuItem, deleteElementMenuItem);
        } else {
            FlashcardStack stack = (FlashcardStack) item;
            setText(stack.getTitle());
            setGraphic(iconFlashStack);
            rightClickMenu.getItems().setAll(addFlashcardElementMenuItem, duplicateElementMenuItem, deleteElementMenuItem);
        }
        setContextMenu(rightClickMenu);
    }

    public MenuItem getDeleteElementMenuItem() {
        return deleteElementMenuItem;
    }

    public MenuItem getDuplicateElementMenuItem() {
        return duplicateElementMenuItem;
    }

    public MenuItem getAddFlashcardElementMenuItem() {
        return addFlashcardElementMenuItem;
    }
}
