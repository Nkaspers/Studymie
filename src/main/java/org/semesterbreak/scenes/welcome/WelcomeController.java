package org.semesterbreak.scenes.welcome;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.jdom2.JDOMException;
import org.semesterbreak.Utilities;

import java.io.IOException;

public class WelcomeController {

    public Button addStackButton;
    public Button openProjectButton;

    public void initialize() {
        initializeIcons();
    }

    private void initializeIcons() {
        try {
            Group stackIcon = Utilities.getIconGroup("add_stack_white.svg", true);
            var stackAnchorpane = ((AnchorPane) addStackButton.getGraphic());
            Label stackGraphic = (Label) stackAnchorpane.getChildren().get(0);
            stackIcon.setScaleX(1.5);
            stackIcon.setScaleY(1.5);
            stackGraphic.setGraphic(stackIcon);

            Group openIcon = Utilities.getIconGroup("open.svg", true);
            var openAnchorpane = ((AnchorPane) openProjectButton.getGraphic());
            Label openGraphic = (Label) openAnchorpane.getChildren().get(0);
            openGraphic.setScaleX(1.5);
            openGraphic.setScaleY(1.5);
            openGraphic.setGraphic(openIcon);
        }catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }
}
