package org.semesterbreak;

import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class CustomFlashcardCell extends ListCell<FlashcardPane> implements EventHandler<MouseEvent> {
    private HBox hBox;
    public CustomFlashcardCell() {
        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
    }

    @Override
    protected void updateItem(FlashcardPane item, boolean empty) {
        super.updateItem(item, empty);
        if(empty) {
            setGraphic(null);
            return;
        }

        if(isSelected()) {
            item.setSelected(true);
        }else {
            item.setSelected(false);
        }
        item.setWebViewOnMousePressedListener(this);

        hBox.getChildren().setAll(item);
        setGraphic(hBox);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        getListView().getSelectionModel().select(this.getItem());
    }
}
