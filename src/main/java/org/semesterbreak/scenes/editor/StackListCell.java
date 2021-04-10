package org.semesterbreak.scenes.editor;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import org.jdom2.JDOMException;
import org.semesterbreak.FlashcardStack;
import org.semesterbreak.Utilities;

import java.io.IOException;


public class StackListCell extends ListCell<FlashcardStack> {


    private Group stackGraphic;

    public StackListCell(){
        try {
            stackGraphic = Utilities.getIconGroup("src/main/resources/org/semesterbreak/icons/stack_icon.svg");
        } catch (JDOMException | IOException e){
            e.printStackTrace();
        }
    }


    @Override
    protected void updateItem(FlashcardStack stack, boolean empty) {
        super.updateItem(stack, empty);

        if(empty || stack == null) {
            setText(null);
            setGraphic(null);

        } else {
            setText(stack.getTitle());
            setGraphic(stackGraphic);
        }

    }

}
