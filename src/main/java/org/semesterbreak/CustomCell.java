package org.semesterbreak;

import javafx.scene.Group;
import javafx.scene.control.TreeCell;
import javafx.scene.control.cell.TextFieldTreeCell;
import org.jdom2.JDOMException;

import java.io.IOException;

public class CustomCell extends TreeCell<TreeViewElement> {
    private Group iconFlash;
    private Group iconFlashSelected;
    private Group iconFlashStack;

    public CustomCell() {
        try {
            iconFlashSelected = Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard_selected.svg");
            iconFlash = Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/preview_flashcard.svg");
            iconFlashStack = Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/stack_icon.svg");
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(TreeViewElement item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
            return;
        }
        if (item.isFlashcard()) {
            Flashcard flashcard = (Flashcard) item;
            setText(flashcard.question);
            if (isSelected()) {
                setGraphic(iconFlashSelected);
            } else {
                setGraphic(iconFlash);
            }
        } else {
            FlashcardStack stack = (FlashcardStack) item;
            setText(stack.getTitle());
            setGraphic(iconFlashStack);
        }
    }
}
